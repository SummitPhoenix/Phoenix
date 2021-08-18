package com.sparkle.util;

import java.io.UnsupportedEncodingException;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/11/27 下午3:29
 */
public class test {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String url = "http://pushplus.hxtrip.com/send?token=cace7b6e38db41e5acb7997f4efe6122&title=均线提醒&content=1&template=test";
//        url = URLEncoder.encode(url, StandardCharsets.ISO_8859_1.name());
        System.out.println(url);
        String response = HttpClientUtil.sendRequest(url, null);
        System.out.println(response);
    }

    /**
     * 找到该值在数组中的下标，否则返回-1
     *
     * @param array
     * @param key
     * @return
     */
    public static int binarySearch(int[] array, int key) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] == key) {
                return mid;
            } else if (array[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}
