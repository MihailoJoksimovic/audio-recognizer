package com.mihailojoksimovic.service;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class PlayerService {
    private static PlayerService instance;

    public static PlayerService getInstance() {
        if (instance == null) {

            // TODO: How to do dependency injection?

            instance = new PlayerService();
        }

        return instance;
    }

    public void play(short[] samples, AudioFormat audioFormat) throws LineUnavailableException {
        DataLine.Info info              = new DataLine.Info(SourceDataLine.class, audioFormat);

        SourceDataLine sourceDataLine   = (SourceDataLine) AudioSystem.getLine(info);

        sourceDataLine.open();

        sourceDataLine.start();

        for (int i = 0; (i + 2) < samples.length; i+=2) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(4).order((audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN));

            byteBuffer.putShort(samples[i]);
            byteBuffer.putShort(samples[i+1]);

            byte[] bytes          = byteBuffer.array();

            sourceDataLine.write(bytes, 0, bytes.length);
        }

        sourceDataLine.drain();
        sourceDataLine.close();
    }

}
