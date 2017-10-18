package com.mihailojoksimovic.service;

import com.mihailojoksimovic.model.Peak;
import com.mihailojoksimovic.model.Point;

/**
 * Created by mihailojoksimovic on 10/17/17.
 */
public class PointsFromPeaksCreator {

    private static PointsFromPeaksCreator instance;

    public static PointsFromPeaksCreator getInstance() {
        if (instance == null) {
            instance = new PointsFromPeaksCreator();
        }

        return instance;
    }

    public static Point[] makePointsFromPeaks(Peak[] peaks, int distance) {
        // Simply connect all points with all peaks (except in the current freq. bin)

        Point[] points = new Point[peaks.length * peaks.length];

        int counter     = 0;

        for (int i = 0; i < peaks.length; i++) {
            Peak peak1 = peaks[i];

            for (int j = i+1; j < (peaks.length - 1); j++) {
                Peak peak2 = peaks[j];

                if (peak2 == null) {
                    break;
                }

                int deltaTimebin    = peak2.getTimeBin() - peak1.getTimeBin();

                if (deltaTimebin == 0) {
                    // Skip peaks in same freq. timebin
                    continue;
                }

                if (deltaTimebin > distance) {
                    break;
                }

                Point point         = new Point(peak1, peak2, deltaTimebin, i);

                points[counter++]   = point;
            }
        }

        // Since I'm too retarded to figure out how many points are there, this was easier to implement :-)
        Point[] finalPoints = new Point[counter-1];

        System.arraycopy(points, 0, finalPoints, 0, finalPoints.length);

        return finalPoints;
    }
}
