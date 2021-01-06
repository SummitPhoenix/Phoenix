package com.sparkle.entity;

import lombok.Data;

@Data
public class TransactionOrder {
    /**
     * 订单号
     */
    private String id;
    /**
     * 股票代码
     */
    private String stockCode;
    /**
     * 委托方向
     */
    private String entrustDirection;
    /**
     * 报价类型
     */
    private String offerType;
    /**
     * 委托价格
     */
    private String entrustPrice;
    /**
     * 目标类型
     */
    private String targetType;
    /**
     * 委托总量
     */
    private String delegateTotal;
    /**
     * 可买数量
     */
    private String buyAbleNumber;
    /**
     * 分配策略
     */
    private String allocationStrategy;

}