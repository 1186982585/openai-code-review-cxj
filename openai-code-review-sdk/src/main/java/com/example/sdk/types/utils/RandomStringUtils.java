package com.example.sdk.types.utils;

import java.util.Random;

/**
 * @Author cxj
 * @Date 2025/4/27 14:17
 * @Description:
 */
public class RandomStringUtils {

    public static String randomNumeric(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
