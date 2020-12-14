package com.sparkle.util;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/11/27 下午3:29
 */
public class test {

    public static void main(String[] args) {
        String codePrefix = "";
        int codeLength = 13;
        int newCounterValue = 14;
        codePrefix = codePrefix + String.format("%0" + codeLength + "d", newCounterValue);

        System.out.println(codePrefix);

//        double[] worth = new double[]{1.0, 1.01, 1.05, 1.1, 1.11, 1.07, 1.03, 1.03, 1.01, 1.02, 1.04, 1.05, 1.07, 1.06, 1.05, 1.06, 1.07, 1.09, 1.12, 1.18, 1.15, 1.15, 1.18, 1.16, 1.19, 1.17, 1.17, 1.18, 1.19, 1.23, 1.24, 1.25, 1.24, 1.25, 1.24, 1.25, 1.24, 1.25, 1.24, 1.27, 1.23, 1.22, 1.18, 1.2, 1.22, 1.25, 1.25, 1.27, 1.26, 1.31, 1.32, 1.31, 1.33, 1.33, 1.36, 1.33, 1.35, 1.38, 1.4, 1.42, 1.45, 1.43, 1.46, 1.48, 1.52, 1.53, 1.52, 1.55, 1.54, 1.53, 1.55, 1.54, 1.52, 1.53, 1.53, 1.5, 1.45, 1.43, 1.42, 1.41, 1.43, 1.42, 1.45, 1.45, 1.49, 1.49, 1.51, 1.54, 1.53, 1.56, 1.52, 1.53, 1.58, 1.58, 1.58, 1.61, 1.63, 1.61, 1.59};
//        BigDecimal maxDrawDown = BigDecimal.valueOf(0.0);
//        BigDecimal tempMaxValue = BigDecimal.valueOf(1.0);
//        for (int i = 0; i < worth.length; i++) {
//            BigDecimal value = BigDecimal.valueOf(worth[i]);
//            tempMaxValue = Decimal.max(tempMaxValue, value);
//            BigDecimal rate = value.divide(tempMaxValue, 3, RoundingMode.HALF_UP).subtract(BigDecimal.valueOf(1.0));
//            maxDrawDown = Decimal.min(maxDrawDown, rate);
//        }
//        maxDrawDown = maxDrawDown.multiply(BigDecimal.valueOf(100.0)).setScale(1, RoundingMode.HALF_UP);
//        System.out.println(maxDrawDown);
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
