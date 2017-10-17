package com.mihailojoksimovic.service;

import com.mihailojoksimovic.SongProcessor;
import com.mihailojoksimovic.service.math.Complex;
import com.mihailojoksimovic.service.math.FFT;
import com.mihailojoksimovic.service.math.Util;
import com.mihailojoksimovic.service.windowing.HammingWindow;

import javax.sound.sampled.AudioFormat;

/**
 * Takes array of samples, and returns array
 * of frequencies, effectively applying
 * windowing function
 */
public class TimeToFrequencyDomainConverter {

    private static TimeToFrequencyDomainConverter instance;

    public static TimeToFrequencyDomainConverter getInstance() {
        if (instance == null) {

            // TODO: How to do dependency injection?

            instance = new TimeToFrequencyDomainConverter();
        }

        return instance;
    }

    /**
     * In a returned array, first index is time position, which, if translated to time
     * is equal to half window size in milliseconds. Second parameter is unfiltered
     * array of frequency bins
     *
     * @param samples
     * @return
     */
    public double[][] convertToFrequencyDomain(short[] samples, AudioFormat audioFormat, int windowSizeInMs) {
        final int windowSizeInSamples       = (int) Math.floor((audioFormat.getSampleRate() / 1000) * windowSizeInMs);
        final int halfWindowSizeInSamples   = windowSizeInSamples / 2;

        System.out.println("Window size in samples: "+windowSizeInSamples+"; half window size: "+halfWindowSizeInSamples+"; FFT chunk size: "+windowSizeInSamples);

        final int numResults    = (samples.length / halfWindowSizeInSamples);

        double[][] results      = new double[numResults][];

        int counter             = 0;

        for (int i = 0; (i+windowSizeInSamples) <= samples.length; i+=halfWindowSizeInSamples) {
            short[] chunks          = new short[windowSizeInSamples];
            Complex[] cSamples      = new Complex[Util.largestPowerOf2(windowSizeInSamples)];

            System.arraycopy(samples, i, chunks, 0, windowSizeInSamples);

            for (int j = 0; j < windowSizeInSamples; j++) {
                System.out.print(chunks[j] + "\t");

                cSamples[j] = new Complex(chunks[j], 0);
            }

            // Fill in the diff with 0-values
            for (int j = windowSizeInSamples; j < cSamples.length; j++) {
                cSamples[j] = new Complex(0, 0);
            }

            System.out.println();

            Complex[] frequencyBins = FFT.fft(cSamples);

            // Convert frequency bins to array
            double[] mags           = new double[cSamples.length];

            System.out.println();
            System.out.println();

            for (int j = 0; j < cSamples.length / 2; j++) {
                mags[j] = frequencyBins[j].abs();

                System.out.println(j + "\t" + mags[j]);
            }

            System.out.println();
            System.out.println();

            results[counter++]  = mags;
        }

        // Fill in the gaps
        for (int i = counter; i < results.length; i++) {
            results[i]  = new double[windowSizeInSamples];
        }

        return results;
    }
}
