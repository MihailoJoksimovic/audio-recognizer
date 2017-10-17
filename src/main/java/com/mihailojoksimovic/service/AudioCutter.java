package com.mihailojoksimovic.service;

import javax.sound.sampled.AudioFormat;

/**
 * Created by mihailojoksimovic on 10/14/17.
 */
public class AudioCutter {
    /**
     * Cuts the part of audio signal, specified by offset in seconds
     *
     */
    public static short[] cutAudio(short[] samples, AudioFormat audioFormat, double offsetInMilliseconds, double lengthInMilliseconds) {
        double sampleRateInMilliseconds    = audioFormat.getSampleRate() / 1000;

        int offset   = (int) (offsetInMilliseconds * sampleRateInMilliseconds);
        int length   = (int) (lengthInMilliseconds * sampleRateInMilliseconds);

        short[] cut = new short[length];

        if ((offset + length) > samples.length) {
            System.arraycopy(samples, offset, cut, 0, (samples.length - length));
        } else {
            System.arraycopy(samples, offset, cut, 0, length);
        }

        return cut;
    }

    public static short[] cutAudioByChunks(short[] samples, AudioFormat audioFormat, int offset, int size) {
        short[] cut = new short[size];

        System.arraycopy(samples, offset, cut, 0, size);

        return cut;
    }
}
