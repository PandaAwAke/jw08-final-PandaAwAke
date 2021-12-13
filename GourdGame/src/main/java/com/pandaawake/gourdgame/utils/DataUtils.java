package com.pandaawake.gourdgame.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DataUtils {

    public static byte[] intToBytes(int num) {
        byte[] result = new byte[4];
        result[0] = (byte)((num >>> 24) & 0xff);
        result[1] = (byte)((num >>> 16) & 0xff);
        result[2] = (byte)((num >>> 8)  & 0xff);
        result[3] = (byte)((num)        & 0xff);
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

    public static byte[] floatToBytes(float f) {
        int intBits = Float.floatToIntBits(f);
        return intToBytes(intBits);
    }

    public static float bytesToFloat(byte[] bytes) {
        int intBits = bytesToInt(bytes);
        return Float.intBitsToFloat(intBits);
    }

    public static int getHeaderNumber(byte[] bytes, int index) {
        byte[] numberBytes = new byte[4];
        System.arraycopy(bytes, index, numberBytes, 0, 4);
        return bytesToInt(numberBytes);
    }

    public static byte[] concatBytes(byte[]... dataList) throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        for (byte[] data: dataList) {
            oStream.write(data);
        }
        return oStream.toByteArray();
    }

    public static byte[] addLengthHeader(byte[] data) throws IOException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        oStream.write(intToBytes(data.length));
        oStream.write(data);
        return oStream.toByteArray();
    }


}
