package com.mihailojoksimovic.service;

import org.apache.commons.io.IOUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SamplesExtractor
{
    // Number of channels for Mono & Stereo
    private final int MONO    = 1;
    private final int STEREO  = 2;

    private static SamplesExtractor instance;

    public static SamplesExtractor getInstance() {
        if (instance == null) {
            instance = new SamplesExtractor();
        }

        return instance;
    }

    /**
     * Extract samples from Audio Stream
     *
     * @param stream
     * @return
     * @throws CantExtractSamplesException
     */
    public short[] extractSamplesFromStream(AudioInputStream stream) throws CantExtractSamplesException {
        byte[] bytes    = extractBytes(stream);

        return getSamplesFromBytesArray(bytes, stream.getFormat());
    }

    private byte[] extractBytes(AudioInputStream stream) throws CantExtractSamplesException {
        try {
            byte[] bytes = IOUtils.toByteArray(stream);

            return bytes;
        } catch (IOException ex) {
            throw new CantExtractSamplesException();
        }
    }

    private short[] getSamplesFromBytesArray(byte[] bytes, AudioFormat audioFormat) {
        ByteOrder byteOrder     = audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;

        ByteBuffer byteBuffer   = ByteBuffer.wrap(bytes).order(byteOrder);

        // Number of bytes per sample
        int bytesPerSample      = audioFormat.getSampleSizeInBits() / 8;

        // Number of samples equals to total number of bytes in song, divided by number of bytes per frame
        // Num. of bytes per frame is number of channels multiplied by number of bytes per sample ;)
        short[] samples         = new short[bytes.length / (bytesPerSample * audioFormat.getChannels())];

        int index               = 0;

        while (byteBuffer.hasRemaining()) {
            int left, right, intAvg;

            short avg;

            if (audioFormat.getChannels() == STEREO) {
                left    = byteBuffer.getShort();
                right   = byteBuffer.getShort();

                intAvg  = (left + right) / 2;

                avg     = (short) intAvg;
            } else {
                // If we're on Mono - avg is equal to current amplitude
                avg     = byteBuffer.getShort();
            }

            samples[index++] = avg;
        }

        return samples;
    }
}