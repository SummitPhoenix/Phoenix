package com.sparkle.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @author Smartisan
 */
public interface StockMapper {

    @Select("SELECT stockCode,stockName,type,rate,price,ma5,ma20,ma60,position,CHANGED,updateTime FROM stock WHERE enable=1")
    List<Map<String, Object>> getStockList();

    @Update("UPDATE stock SET rate=#{rate},price=#{price},ma5=#{ma5},ma20=#{ma20},ma60=#{ma60},position=#{position},changed=#{changed} WHERE stockCode=#{stockCode}")
    void updateStock(Map<String, Object> map);

}