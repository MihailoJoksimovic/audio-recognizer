package com.mihailojoksimovic.service;

import com.mihailojoksimovic.model.Peak;

public class PeakExtractor {
    private static int LOWER_RANGE                  = 40;
    private static int UPPER_RANGE                  = 300;
    public final int[] RANGE                        = new int[] { LOWER_RANGE, 80, 120, 180, UPPER_RANGE+1 };

    public PeakExtractor() {

    }

    /**
     * Extracts peaks from time-frequency bins array
     */
    public Peak[] extractPeaks(double[][] timeFrequencyBins) {
        Peak[]  results = new Peak[timeFrequencyBins.length * RANGE.length];

        int counter     = 0;

        for (int i = 0; i < timeFrequencyBins.length; i++) {
            double[] frequencyBins      = timeFrequencyBins[i];

            // Get average amplitude for the resultset
            double avgAmplitude         = getAvgAmplitude(frequencyBins);

            System.out.println("Average amplitude is: "+avgAmplitude);

            double[] maxAmplitudes      = new double[RANGE.length];
            int[] maxFreqs              = new int[RANGE.length];

            // Find highest freqs in ranges
            for (int j = 0; j < frequencyBins.length; j++) {
                if (frequencyBins[j] < avgAmplitude) {
                    continue;
                }

                int index   = getIndex(j);

                if (frequencyBins[j] > maxAmplitudes[index]) {
                    maxAmplitudes[index]    = frequencyBins[j];
                    maxFreqs[index]         = j;
                }
            }

            for (int j = 0; j < maxFreqs.length; j++) {
                if (maxFreqs[j] > 0) {
                    results[counter++]  = new Peak(maxFreqs[j], i);
                }
            }
        }

        return results;
    }

    private double getAvgAmplitude(double[] frequencyBins) {
        double sum  = 0;
        int count   = 0;

        for (int i = 0; i < frequencyBins.length; i++) {
            if (frequencyBins[i] > 0) {
                sum += frequencyBins[i];
                count++;
            }
        }

        return sum / (double) count;
    }

    private int getIndex(double frequencyBin) {
        for (int i = 0; i < RANGE.length; i++) {
            if (frequencyBin < RANGE[i]) {
                return i;
            }
        }

        return (RANGE.length - 1);
    }



}
