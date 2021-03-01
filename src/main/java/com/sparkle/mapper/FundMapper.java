package com.sparkle.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @author Smartisan
 */
public interface FundMapper {

    @Select("SELECT fundCode,fundName,rate,worth,day5,day10,day20,updateTime FROM fund")
    List<Map<String, Object>> getFundList();

    @Update("UPDATE fund SET fundName=#{fundName},rate=#{rate},worth=#{worth},day5=#{day5},day10=#{day10},day20=#{day20},day60=#{day60},year=#{year},maxDrawDown=#{maxDrawDown} WHERE fundCode=#{fundCode}")
    void updateFund(Map<String, Object> map);

    void updateFundBatch(Map<String, Object> map);

    List<Map<String, Object>> getPosition(String userId, String fundCode, String status);

    void insertPosition(Map<String, Object> map);

    void updatePosition(Map<String, Object> map);
}