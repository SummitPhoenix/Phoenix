package com.sparkle.util;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 东方财富涨停统计接口爬虫筛选
 * http://quote.eastmoney.com/ztb/detail#type=qsgc
 */
public class StockDailyLimitStatistics {
    private static final BigDecimal billion50 = BigDecimal.valueOf(5000000000.0);
    private static final BigDecimal billion100 = BigDecimal.valueOf(10000000000.0);
    private static final BigDecimal billion1000 = BigDecimal.valueOf(100000000000.0);
    private static final BigDecimal billion2000 = BigDecimal.valueOf(200000000000.0);

    private static Set<String> stocks = new HashSet<>();

    public static void main(String[] args) throws Exception {
        String url = "http://push2ex.eastmoney.com/getTopicQSPool?ut=7eea3edcaed734bea9cbfc24409ed989&dpt=wz.ztzt&Pageindex=0&pagesize=500&sort=tshare%3Adesc&date=" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        BigDecimal minTotalMarket = billion50;
        BigDecimal minRate = BigDecimal.valueOf(5.0);
        BigDecimal minTurnOver = BigDecimal.valueOf(0.1);
        BigDecimal maxTurnOver = BigDecimal.valueOf(90.0);

        //定时任务线程池60秒触发一次更新数据
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            //交易时间结束关闭线程池
            if (now.after(StockUtil.afternoonEnd)) {
                System.exit(0);
            }
            //非交易时间不执行
            if (!StockUtil.effectiveTime(now)) {
                return;
            }
            //概念
            try {
                String info = analyse(url, minTotalMarket, minRate, minTurnOver, maxTurnOver, 1);
                if (StringUtils.hasText(info)) {
                    StockUtil.windowsMessagePush(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 60, TimeUnit.SECONDS);

    }

    /**
     * @param url              接口地址
     * @param minTotalMarket   最小市值
     * @param minRate          最低涨幅
     * @param minTurnOver      最低换手
     * @param maxTurnOver      最高换手
     * @param dailyLimitNumber 涨停次数
     */
    private static String analyse(String url, BigDecimal minTotalMarket, BigDecimal minRate, BigDecimal minTurnOver, BigDecimal maxTurnOver, int dailyLimitNumber) {
        String originData = HttpClientUtil.sendRequest(url, null);
        originData = originData.substring(originData.indexOf("[{"), originData.lastIndexOf("}}"));
        List<Map<String, Object>> data = (List<Map<String, Object>>) JSON.parse(originData);

        //筛选市值 涨幅 涨停统计
        data = data.stream().filter(stock ->
                Decimal.min(minTotalMarket, (BigDecimal) stock.get("tshare")).compareTo(minTotalMarket) == 0 &&
                        Decimal.max((BigDecimal) stock.get("tshare"), billion2000).compareTo(billion2000) == 0 &&
                        ((BigDecimal) stock.get("zdp")).compareTo(minRate) > 0 &&
                        ((BigDecimal) stock.get("zdp")).compareTo(BigDecimal.valueOf(11.0)) < 0 &&
                        ((BigDecimal) stock.get("hs")).compareTo(minTurnOver) > 0 &&
                        ((BigDecimal) stock.get("hs")).compareTo(maxTurnOver) < 0 &&
                        ((Map<String, Integer>) stock.get("zttj")).get("ct") >= dailyLimitNumber &&
                        new BigDecimal(stock.get("amount") + "").compareTo(BigDecimal.valueOf(500000000)) > 0
        ).collect(Collectors.toList());
        String pushInfo = "";
        for (Map<String, Object> stock : data) {
            String stockCode = (String) stock.get("c");
            if (stockCode.startsWith("300") || stockCode.startsWith("301") || stockCode.startsWith("688")) {
                continue;
            }
            String stockName = (String) stock.get("n");
            //涨跌幅
            BigDecimal rise = ((BigDecimal) stock.get("zdp")).setScale(2, RoundingMode.HALF_UP);
            //版块
            String industrySector = (String) stock.get("hybk");
            //现价
            double currentPrice = (Integer) stock.get("p") / 1000.0;
            //成交额
            BigDecimal amount = new BigDecimal(stock.get("amount") + "").divide(BigDecimal.valueOf(100000000.0), 2, RoundingMode.HALF_UP);
            //换手
            BigDecimal turnOver = ((BigDecimal) stock.get("hs")).setScale(2, RoundingMode.HALF_UP);
            //总市值
            BigDecimal totalMarket = ((BigDecimal) stock.get("tshare")).divide(BigDecimal.valueOf(100000000.0), 2, RoundingMode.HALF_UP);
            //涨停统计
            int dailyLimitCount = ((Map<String, Integer>) stock.get("zttj")).get("ct");

            String info = stockCode + " " + stockName + " " + rise + "% " + industrySector + " 现价：" + currentPrice + " 成交额：" + amount + "亿 换手：" + turnOver + "% 总市值：" + totalMarket + "亿 涨停：" + dailyLimitCount;
            System.out.println(info);
            if (stocks.contains(stockCode)) {
                continue;
            }
            stocks.add(stockCode);
            pushInfo += info + "\n";
        }
        return pushInfo;
    }

}