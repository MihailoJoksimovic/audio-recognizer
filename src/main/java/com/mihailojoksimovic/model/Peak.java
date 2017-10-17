package com.mihailojoksimovic.model;

public class Peak
{
    private int frequencyBin;

    /**
     * Each time bin is ~25ms
     */
    private int timeBin;

    public Peak(int frequencyBin, int timeBin) {
        this.frequencyBin = frequencyBin;
        this.timeBin = timeBin;
    }

    public int getFrequencyBin() {
        return frequencyBin;
    }

    public void setFrequencyBin(int frequencyBin) {
        this.frequencyBin = frequencyBin;
    }

    public int getTimeBin() {
        return timeBin;
    }

    public void setTimeBin(int timeBin) {
        this.timeBin = timeBin;
    }
}
