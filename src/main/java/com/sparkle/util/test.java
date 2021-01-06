package com.sparkle.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/11/27 下午3:29
 */
public class test {

    public static void main(String[] args) {
        File file = new File("");
        Calendar calendar = Calendar.getInstance();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 60);
        Date deadline = calendar.getTime();
        String createdDate = new SimpleDateFormat("yyyy-MM-dd").format(deadline);
        System.out.println(createdDate);
    }


    public static int hashCode(char[] value, int hash) {
        int h = hash;
        if (h == 0 && value.length > 0) {
            for (char c : value) {
                h = 31 * h + c;
            }
        }
        return h;
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
