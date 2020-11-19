package com.sparkle.util;

import java.math.BigDecimal;

/**
 * 1.印花税:成交金额的0.1%
 * 2.证管费:成交金额的0.002%双向收取
 * 3.证券交易经手费:0.00487%双向收取
 * 4.过户费:按成交金额的0.02‰
 * 5.券商交易佣金:按成交金额的0.013%
 */
public class CalculateUtil {

    private static final double stampTax = 0.001;
    private static final double transferFee = 0.0002;
    private static final double brokerCommissions = 0.00013;

    private static final double administrativeFee = 0.00002;
    private static final double brokerage = 0.0000487;

    public static double buyServiceCharge(double money) {
        double percent = add(administrativeFee, brokerage);
        System.out.println("buy percent:" + percent * 100);
        return Decimal.multiply(money, percent);
    }

    public static double sellServiceCharge(double money) {
        double percent = add(stampTax, transferFee);
        percent = add(percent, brokerCommissions);
        percent = add(percent, administrativeFee);
        percent = add(percent, brokerage);
        System.out.println("sell percent:" + percent * 100);
        return Decimal.multiply(money, percent);
    }

    public static double add(double num1, double num2) {
        BigDecimal decimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal decimal2 = new BigDecimal(String.valueOf(num2));
        BigDecimal result = decimal1.add(decimal2);
        return result.doubleValue();
    }
}
