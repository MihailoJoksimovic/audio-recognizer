package com.mihailojoksimovic.util;

import com.mihailojoksimovic.service.AudioCutter;

import javax.sound.sampled.AudioFormat;

/**
 * Created by mihailojoksimovic on 10/14/17.
 */
public class TestCutter {
    public static void main(String[] args) {
        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                1000,
                16,
                1,
                1,
                1,
                true
        );

        short[] original = new short[1000];

        for (int i = 0; i < original.length; i++) {
            original[i] = (short) i;
        }

        for (int i = 0; (i+40) <= original.length; i+=25) {
            short[] cutted = AudioCutter.cutAudio(original, audioFormat, i, 25);

            for (int j = 0; j < cutted.length; j++) {
                System.out.println(cutted[j]);
            }
        }

//        short[] cutted  = AudioCutter.cutAudio(original, audioFormat, 0, 25);

        int a = 5;
    }
}
