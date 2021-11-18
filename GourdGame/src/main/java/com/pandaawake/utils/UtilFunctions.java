package com.pandaawake.utils;

import com.pandaawake.Config;

import java.util.Random;

public class UtilFunctions {

    private static Random random = new Random();

    public static boolean getRandomResultByProbability(float probability) {
        int number = random.nextInt(65536);
        if (number <= probability * 65535) {
            return true;
        }
        return false;
    }
    public static float getRandomProbability() {
        int number = random.nextInt(65536);
        return (float) number / 65535.0f;
    }

}
