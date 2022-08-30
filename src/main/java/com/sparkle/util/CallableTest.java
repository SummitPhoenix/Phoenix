package com.sparkle.util;

import java.util.concurrent.Callable;

public class CallableTest {
    public static void main(String[] args) throws Exception {
        if (new Callable<Boolean>() {
            @Override
            public Boolean call() {
                System.out.println("a");
                return false;
            }
        }.call()) {
            System.out.println("a");
        } else {
            System.out.println("b");
        }
    }
}