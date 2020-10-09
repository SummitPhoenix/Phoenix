package com.sparkle.util;

import java.security.MessageDigest;

public class PasswordEncode {
    public static String encode(String key, String data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        byte[] dataArray = data.getBytes("UTF-8");
        byte[] keyArray = key.getBytes("UTF-8");

        md5.update(keyArray);
        byte[] md5Key = md5.digest();

        sha.update(dataArray);
        sha.update(md5Key);
        byte[] shaBytes = sha.digest();

        for (int j = 0; j < 5000; j++) {
            sha.update(shaBytes);
            shaBytes = sha.digest();
        }

        StringBuilder hexValue = new StringBuilder();

        for (byte shaByte : shaBytes) {
            int val = ((int) shaByte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static String convert(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }

    public static void main(String[] args) throws Exception {
        String code = encode("admin", "123");
        System.out.println(code);
        code = convert(code);
        System.out.println(code);
        code = convert(code);
        System.out.println(code);
    }
}
