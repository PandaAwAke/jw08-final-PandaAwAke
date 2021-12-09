package com.pandaawake.gourdgame.utils;

import java.io.ByteArrayOutputStream;
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

    public static byte[] intToBytes(int num) {
        byte[] result = new byte[4];
        result[0] = (byte)((num >>> 24) & 0xff);
        result[1] = (byte)((num >>> 16) & 0xff);
        result[2] = (byte)((num >>> 8)  & 0xff);
        result[3] = (byte)((num >>> 0)  & 0xff);
        return result;
    }

    public static int bytesToInt(byte[] bytes) {
        int result = 0;
        if (bytes.length == 4) {
            int a = (bytes[0] & 0xff) << 24;
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }

    public static int getHeaderNumber(byte[] bytes, int index) {
        byte[] numberBytes = new byte[4];
        System.arraycopy(bytes, index, numberBytes, 0, 4);
        return bytesToInt(numberBytes);
    }

    public static byte[] concatBytes(byte[] data1, byte[] data2) {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        int off = 0;
        oStream.write(data1, off, data1.length);
        off += data1.length;

        oStream.write(data2, off, data2.length);
        off += data2.length;

        return oStream.toByteArray();
    }


}
