package com.sparkle.util;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/11/27 下午3:29
 */
public class test {

    public static void main(String[] args) {
        int[] array = {0,1,2,3,4,5,6,7,8,9};
        System.out.println(binarySearch(array,5));
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
