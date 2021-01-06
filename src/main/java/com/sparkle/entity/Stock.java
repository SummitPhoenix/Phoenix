package com.sparkle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Smartisan
 */
@Data
@AllArgsConstructor
public class Stock {
    /**
     * 股票代码
     */
    private String stockCode;
    /**
     * 股票名
     */
    private String stockName;
    /**
     * 价格
     */
    private double price;

    private double[] sell;

    private double[] buy;
    /**
     * 昨收
     */
    private double yesterdayClosed;
    /**
     * 开盘
     */
    private double openingPrice;
    /**
     * 涨停
     */
    private double raisingLimit;
    /**
     * 跌停
     */
    private double limitDown;

}