package com.sparkle.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author K1181378
 */
public class Decimal {

    public static double add(double num1, double num2) {
        BigDecimal decimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal decimal2 = new BigDecimal(String.valueOf(num2));
        BigDecimal result = decimal1.add(decimal2).setScale(2, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    public static double subtract(double num1, double num2) {
        BigDecimal decimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal decimal2 = new BigDecimal(String.valueOf(num2));
        BigDecimal result = decimal1.subtract(decimal2).setScale(2, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    public static double multiply(double num1, double num2) {
        BigDecimal decimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal decimal2 = new BigDecimal(String.valueOf(num2));
        BigDecimal result = decimal1.multiply(decimal2).setScale(2, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    public static double divide(double num1, double num2) {
        BigDecimal decimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal decimal2 = new BigDecimal(String.valueOf(num2));
        BigDecimal result = decimal1.divide(decimal2, 2, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    public static String displayPrice(double price) {
        return new DecimalFormat("#0.00").format(price);
    }

    public static void main(String[] args) {
        System.out.println(multiply(12, 34));
        System.out.println(divide(12.12, 34.23));
    }
}
