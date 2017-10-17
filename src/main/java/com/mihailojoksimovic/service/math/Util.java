package com.mihailojoksimovic.service.math;

/**
 * Created by mihailojoksimovic on 10/14/17.
 */
public class Util {
    public static int largestPowerOf2 (int n)
    {
        int res = 2;
        while (res < n) {
            res *= 2;
        }

        return res;
    }
}
