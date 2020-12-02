package com.sparkle.util;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author Smartisan
 */
@Slf4j
public class Aes {

    /**
     * 密钥
     */
    private static final String KEY = "4%YkW!@g5LGcf9Ut";
    private static final byte[] KEY_BYTES = KEY.getBytes(StandardCharsets.UTF_8);

    /**
     * 算法/加密模式/填充方式
     */
    private static final String MODE = "AES/ECB/PKCS5Padding";


    public static String encrypt(String str) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY_BYTES, "AES");
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return new BASE64Encoder().encode(encrypted);
        } catch (Exception e) {
            return null;
        }

    }

    public static String decrypt(String str) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY_BYTES, "AES");
            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // 先用base64解密
            byte[] encrypted = new BASE64Decoder().decodeBuffer(str);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(new String(KEY_BYTES));
        String password = "aptx4869";
        password = encrypt(password);
        System.out.println(password);
        password = decrypt(password);
        System.out.println(password);
    }
}
