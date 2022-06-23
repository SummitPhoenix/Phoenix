package com.sparkle.util;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 东方财富涨停统计接口爬虫筛选
 * http://quote.eastmoney.com/ztb/detail#type=qsgc
 */
public class StockSectionStatistics {

    private static LinkedHashMap<String, BigDecimal> m0 = new LinkedHashMap<>();
    private static LinkedHashMap<String, BigDecimal> m5 = new LinkedHashMap<>();
    private static LinkedHashMap<String, BigDecimal> m10 = new LinkedHashMap<>();

    public static void main(String[] args) {
        analyse();
        m0 = sortMapByValue(m0);
        m5 = sortMapByValue(m5);
        m10 = sortMapByValue(m10);

        System.out.println();
        System.out.println("5日： " + m5);
        System.out.println("10日：" + m10);
        System.out.println();
        System.out.println("0日即时： " + m0);
        System.out.println("5日连续： " + intersection(m0, m5));
        System.out.println("10日连续：" + intersection(m0, m10));
    }

    private static void analyse() {
        List<Map<String, Object>> data = getData();
        for (Map<String, Object> bk : data) {
            String bkName = (String) bk.get("f14");

            BigDecimal f0 = ((BigDecimal) bk.get("f62")).divide(BigDecimal.valueOf(100000000.0), 2, RoundingMode.HALF_UP);
            BigDecimal f5 = ((BigDecimal) bk.get("f164")).divide(BigDecimal.valueOf(100000000.0), 2, RoundingMode.HALF_UP);
            BigDecimal f10 = ((BigDecimal) bk.get("f174")).divide(BigDecimal.valueOf(100000000.0), 2, RoundingMode.HALF_UP);
            m0.put(bkName, f0);
            m5.put(bkName, f5);
            m10.put(bkName, f10);
        }
    }

    private static List<Map<String, Object>> getData() {
        String url = "https://push2.eastmoney.com/api/qt/clist/get?pn=1&pz=500&po=1&np=1&fields=f12%2Cf13%2Cf14%2Cf62%2Cf164%2Cf174&fid=f62&fs=m%3A90%2Bt%3A2&ut=b2884a393a59ad64002292a3e90d46a5";
        String originData = HttpClientUtil.sendRequest(url, null);
        originData = originData.substring(originData.indexOf("[{"), originData.lastIndexOf("}}"));
        List<Map<String, Object>> data = (List<Map<String, Object>>) JSON.parse(originData);
        return data;
    }

    private static LinkedHashMap<String, BigDecimal> sortMapByValue(LinkedHashMap<String, BigDecimal> map) {
        //map.entrySet()转换成list
        List<Map.Entry<String, BigDecimal>> list = new ArrayList<>(map.entrySet());
        //降序 比较器
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        LinkedHashMap<String, BigDecimal> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, BigDecimal> mapping : list) {
            //筛选大于1亿的
            if (Decimal.min(BigDecimal.ONE, mapping.getValue()).compareTo(BigDecimal.ONE) != 0) {
                break;
            }
            sortedMap.put(mapping.getKey(), mapping.getValue());
        }
        return sortedMap;
    }

    /**
     * 交集
     */
    private static LinkedHashMap<String, BigDecimal> intersection(LinkedHashMap<String, BigDecimal> map1, LinkedHashMap<String, BigDecimal> map2) {
        LinkedHashMap<String, BigDecimal> intersectionMap = new LinkedHashMap<>();
        for (String key : map1.keySet()) {
            if (map2.containsKey(key)) {
                intersectionMap.put(key, map2.get(key));
            }
        }
        return intersectionMap;
    }
}