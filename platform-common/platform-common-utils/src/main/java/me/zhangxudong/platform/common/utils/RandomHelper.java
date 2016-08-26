package me.zhangxudong.platform.common.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 */
public class RandomHelper {

    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 长度32, 中间无-分割.
     *
     * @return 一个随机码, 形如:5ec24ed3ff1a41c18d23a37af006bbb3
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成一个随机码,长度36.
     *
     * @return 一个随机码, 形如:5ec24ed3-ff1a-41c1-8d23-a37af006bbb3
     */
    public static String randomString() {
        return randomStringByUUID();
    }

    /**
     * 生成一个随机码,长度36,全大写.
     *
     * @return 一个随机码, 形如:5EC24ED3-FF1A-41C1-8D23-A37AF006BBB3
     */
    public static String randomStringUpper() {
        return randomString().toUpperCase();
    }


    private static String randomStringByUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        long number = random.nextLong();
        if (Long.MIN_VALUE == number) {
            return Long.MAX_VALUE;
        } else {
            return Math.abs(number);
        }
    }

    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     */
    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return EncodeHelper.encodeBase62(randomBytes);
    }

    /**
     * 指定位数数字
     * @param charCount
     * @return
     */
    public static String getRandNum(int charCount) {
        StringBuffer charValue = new StringBuffer();
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue.append(String.valueOf(c));
        }
        return charValue.toString();
    }

    public static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println(getRandNum(4));
        }
    }

}
