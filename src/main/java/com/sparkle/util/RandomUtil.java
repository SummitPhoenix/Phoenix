package com.sparkle.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomUtil {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        List<String> list = new ArrayList<>(Arrays.asList("512480", "512690", "159837", "512690"));
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        System.out.println(list.get(secureRandom.nextInt(list.size())));
    }
}