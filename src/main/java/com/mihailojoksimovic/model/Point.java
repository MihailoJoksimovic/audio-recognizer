package com.mihailojoksimovic.model;


public class Point
{
    private Peak peak1;
    private Peak peak2;
    private int deltaTimebin;
    private String song;
    private int timeBin;

    public Point(Peak peak1, Peak peak2, int deltaTimebin, int timeBin) {
        this.peak1 = peak1;
        this.peak2 = peak2;
        this.deltaTimebin = deltaTimebin;
        this.timeBin = timeBin;
    }

    public Point(Peak peak1, Peak peak2, int deltaTimebin, String song) {
        this.peak1 = peak1;
        this.peak2 = peak2;
        this.deltaTimebin = deltaTimebin;
        this.song = song;
    }

    public Peak getPeak1() {
        return peak1;
    }

    public void setPeak1(Peak peak1) {
        this.peak1 = peak1;
    }

    public Peak getPeak2() {
        return peak2;
    }

    public void setPeak2(Peak peak2) {
        this.peak2 = peak2;
    }

    public int getDeltaTimebin() {
        return deltaTimebin;
    }

    public void setDeltaTimebin(int deltaTimebin) {
        this.deltaTimebin = deltaTimebin;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public int getTimeBin() {
        return timeBin;
    }

    public void setTimeBin(int timeBin) {
        this.timeBin = timeBin;
    }
}
