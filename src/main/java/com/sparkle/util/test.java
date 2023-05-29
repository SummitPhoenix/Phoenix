package com.sparkle.util;

public class test {

    private static String sendRequestWithRetry() {
        int maxRetries = 10;
        int retries = 0;
        String callbackResult = null;
        while (retries < maxRetries) {
            try {
                System.out.println(retries);
                callbackResult = "test";
                int a = 0 / 0;
            } catch (Exception e) {
                retries++;
                if (retries == maxRetries) {
                    e.printStackTrace();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
        return callbackResult;
    }

    public static void main(String[] args) {
        System.out.println(sendRequestWithRetry());
    }
}
