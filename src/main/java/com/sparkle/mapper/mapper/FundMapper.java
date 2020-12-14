package com.sparkle.mapper.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @author Smartisan
 */
@Mapper
public interface FundMapper {

    @Select("SELECT fundCode,fundName,rate,worth,day5,day10,day20,updateTime FROM fund")
    List<Map<String, Object>> getFundList();

    @Update("UPDATE fund SET fundName=#{fundName},rate=#{rate},worth=#{worth},day5=#{day5},day10=#{day10},day20=#{day20} WHERE fundCode=#{fundCode}")
    void updateFund(Map<String, Object> map);
}
