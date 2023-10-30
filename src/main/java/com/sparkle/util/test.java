package com.sparkle.util;

import cn.hutool.http.HttpRequest;

public class test {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 50; i++) {
            String token = HttpRequest.get("http://localhost:9010/api/getToken?user=enterprise&accessToken=03D76490284CCBAE89BDF665E5CB212A&level=high").execute().body();
            System.out.println(token);
        }
    }
}
