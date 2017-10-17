package com.mihailojoksimovic.service;

/**
 * Created by mihailojoksimovic on 10/14/17.
 */
public class DownsamplerService {

    private static DownsamplerService instance;

    public static DownsamplerService getInstance() {
        if (instance == null) {
            instance = new DownsamplerService();
        }

        return instance;
    }

    public short[] downSample(int fromFrequency, int toFrequency, short[] samples) {
        final int ratio             = Math.round(fromFrequency / toFrequency);

        int newSamplesSizeLength    = (int) Math.ceil(samples.length / ratio);

        short[] downsampledSamples  = new short[newSamplesSizeLength];

        for (int i = 0; i < newSamplesSizeLength; i++) {
            downsampledSamples[i]   = samples[ratio * i];
        }

        return downsampledSamples;

    }
}
