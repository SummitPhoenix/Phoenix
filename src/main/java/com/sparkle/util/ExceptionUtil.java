package com.sparkle.util;

public class ExceptionUtil {
    public static String getExceptionMessage(Exception e) {
        String message = e.getMessage();

        Throwable t = e.getCause();

        while (t != null) {
            message = t.getMessage();
            t = t.getCause();
        }

        return message;
    }
}
