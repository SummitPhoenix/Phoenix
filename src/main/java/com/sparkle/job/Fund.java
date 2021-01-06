package com.sparkle.job;

import com.alibaba.fastjson.JSON;
import com.sparkle.mapper.FundMapper;
import com.sparkle.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Smartisan
 */
@Slf4j
@Service
public class Fund {

    /**
     * 买入费率
     */
    public static final BigDecimal BUY_RATE = BigDecimal.valueOf(0.0015);
    /**
     * 卖出费率
     */
    public static final BigDecimal SALE_RATE = BigDecimal.valueOf(0.005);

    /**
     * 邮件地址
     */
    @Value("${mailList}")
    private String mailList;

    @Resource
    private FundMapper fundMapper;

    /**
     * 周一至周五 14:30执行
     */
//    @Scheduled(cron = "1 30 14 * * 1,2,3,4,5")
    public void fundCheckJob() {
        ThreadPoolExecutor executor = ThreadPoolUtil.getThreadPoolExecutor("Fund-Analyse-%d");

        List<Map<String, Object>> fundInfoList = fundMapper.getFundList();
        List<String> fundList = new ArrayList<>();
        for (Map<String, Object> fundInfo : fundInfoList) {
            String fundCode = (String) fundInfo.get("fundCode");
            fundList.add(fundCode);
        }

        for (String fundCode : fundList) {
            Task task = new Task(fundCode);
            executor.execute(task);
        }
        //关闭线程池
        executor.shutdown();
    }

    /**
     * 计算最大收益
     *
     * @param fundCode 基金代码
     * @param money    初始资金
     * @param month    月数
     */
    private void analyse(String fundCode, int money, int month) {
        //历史数据
        List<Map<String, Object>> worthList = getHistoryData(fundCode);

        //今日实时行情
        Map<String, Object> currentMarket = getCurrentMarket(fundCode);

        //计算多时段收益
        Map<String, Object> multiPeriodProfit = calculateMultiPeriodProfit(worthList, currentMarket);
        fundMapper.updateFund(multiPeriodProfit);

        //获取历史数据分析截止时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -month);
        long startTime = calendar.getTime().getTime();

        worthList = worthList.stream().filter(map -> (long) map.get("x") > startTime).collect(Collectors.toList());


        BigDecimal min = getLowOrder(worthList);

        worthList = worthList.stream().sorted(Comparator.comparing(a -> ((BigDecimal) a.get("y")))).collect(Collectors.toList());
        BigDecimal max = (BigDecimal) worthList.get(worthList.size() - 1).get("y");

        BigDecimal maxRate = max.divide(min, RoundingMode.HALF_UP);
        //计算买入费用
        BigDecimal fund = new BigDecimal(money).multiply(new BigDecimal("0.9985"));
        //计算卖出费用
        BigDecimal saleRate = new BigDecimal("0.995");
        BigDecimal maxProfit = maxRate.multiply(fund).multiply(saleRate).setScale(2, RoundingMode.HALF_UP);
        //计算收益率
        maxRate = maxRate.subtract(new BigDecimal("1")).multiply(new BigDecimal("100"));

        //计算最大回撤
        BigDecimal maxDrawDown = (BigDecimal) multiPeriodProfit.get("maxDrawDown");

        min = min.setScale(2, RoundingMode.HALF_UP);
        max = max.setScale(2, RoundingMode.HALF_UP);
        String fundName = (String) currentMarket.get("name");
        String info = "<br>\r\n";
        info += fundName + "[" + fundCode + "]<br>\r\n";
        info += "最低: " + min + "<br>\r\n";
        info += "最高: " + max + "<br>\r\n";
        info += "涨幅: " + maxRate.doubleValue() + "%<br>\r\n";
        info += "回撤: " + maxDrawDown.doubleValue() + "%<br>\r\n";
        info += "最大收益: " + maxProfit + "<br>\r\n";
        log.info(info.replace("<br>", ""));

        //判断低位标志
        BigDecimal latestWorth = new BigDecimal((String) currentMarket.get("gsz"));
        if (latestWorth.doubleValue() <= min.doubleValue()) {
            String text = fundName + "(" + fundCode + ")净值处于过去" + month + "个月中最低位" + info;
            MailSender.sendMail("[基金收益提醒]", text, mailList.split(","));
        }
        //风险预警
        increaseWarn(fundName, fundCode, worthList, currentMarket);
    }

    /**
     * 历史数据
     */
    public static List<Map<String, Object>> getHistoryData(String fundCode) {
        List<Map<String, Object>> historyData = (List<Map<String, Object>>) DataCache.CACHE.getIfPresent(fundCode);
        if (historyData != null) {
            return historyData;
        }
        String url = "http://fund.eastmoney.com/pingzhongdata/" + fundCode + ".js";
        String js = HttpClientUtil.sendRequest(url);
        String dataNetWorthTrend = js.substring(js.indexOf("[{"), js.indexOf("}];") + 2);
        historyData = (List<Map<String, Object>>) JSON.parse(dataNetWorthTrend);
        DataCache.CACHE.put(fundCode, historyData);
        return historyData;
    }

    /**
     * 今日实时行情
     */
    public static Map<String, Object> getCurrentMarket(String fundCode) {
        String url = "http://fundgz.1234567.com.cn/js/" + fundCode + ".js";
        String json = HttpClientUtil.sendRequest(url);
        json = json.substring(json.indexOf("{"), json.indexOf("}") + 1);
        return (Map<String, Object>) JSON.parse(json);
    }

    /**
     * 获得低位标志
     *
     * @param worthList 历史数据
     * @return 低位净值
     */
    private BigDecimal getLowOrder(List<Map<String, Object>> worthList) {
        List<Double> doubleWorthList = new ArrayList<>();
        List<Double> lowOrders = new ArrayList<>();

        for (Map<String, Object> worth : worthList) {
            BigDecimal value = (BigDecimal) worth.get("y");
            doubleWorthList.add(value.doubleValue());
        }
        //获得净值低位低谷点
        for (int i = 1; i < doubleWorthList.size() - 1; i++) {
            if (doubleWorthList.get(i - 1) > doubleWorthList.get(i) && doubleWorthList.get(i) < doubleWorthList.get(i + 1)) {
                lowOrders.add(doubleWorthList.get(i));
            }
        }
        double lowOrder = 0;
//        Collections.sort(lowOrders);
//        for (int i = 0; i < 4; i++) {
//            lowOrder += lowOrders.get(i);
//        }
//        lowOrder = lowOrder / 4;
        for (double order : lowOrders) {
            lowOrder += order;
        }
        lowOrder = lowOrder / lowOrders.size();
        return new BigDecimal(String.valueOf(lowOrder));
    }

    /**
     * 执行线程
     */
    class Task implements Runnable {
        private final String fundCode;

        public Task(String fundCode) {
            this.fundCode = fundCode;
        }

        @Override
        public void run() {
            analyse(fundCode, 10000, 3);
            log.info("执行task [{}] - {}", fundCode, Thread.currentThread().getName());
        }
    }

    /**
     * 今日涨幅超过4%,最近20个交易日涨幅超过20%推送风险提示
     */
    private void increaseWarn(String fundName, String fundCode, List<Map<String, Object>> worthList, Map<String, Object> currentMarket) {
        BigDecimal latestWorth = new BigDecimal((String) currentMarket.get("gsz"));
        BigDecimal todayRate = new BigDecimal((String) currentMarket.get("gszzl"));

        BigDecimal historyWorth = (BigDecimal) worthList.get(worthList.size() - 20).get("y");
        BigDecimal increaseRate = latestWorth.divide(historyWorth, RoundingMode.HALF_UP);

        String info = "";
        boolean sendMail = false;
        if (todayRate.doubleValue() >= 4) {
            info += " 今日涨幅超过4%";
            sendMail = true;
        }
        if (increaseRate.doubleValue() >= 1.2) {
            info += " 近20个交易日涨幅超过20%";
            sendMail = true;
        }
        if (sendMail) {
            info = fundName + "[" + fundCode + "]" + info;
            MailSender.sendMail(info, "", mailList.split(","));
        }
    }

    /**
     * 计算涨幅
     *
     * @param latestWorth  最新净值
     * @param historyWorth 历史净值
     * @return 涨幅%
     */
    public static BigDecimal getRate(BigDecimal latestWorth, BigDecimal historyWorth) {
        BigDecimal rate = latestWorth.divide(historyWorth, 4, RoundingMode.HALF_UP);
        rate = rate.subtract(new BigDecimal("1"));
        rate = rate.multiply(new BigDecimal("100"));
        return rate.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算时段最大回撤
     */
    private static BigDecimal getMaxDrawDown(List<Map<String, Object>> worthList) {
        List<Double> doubleWorthList = new ArrayList<>();
        for (Map<String, Object> worth : worthList) {
            BigDecimal value = (BigDecimal) worth.get("y");
            doubleWorthList.add(value.doubleValue());
        }

        BigDecimal maxDrawDown = BigDecimal.valueOf(0.0);
        BigDecimal tempMaxValue = BigDecimal.valueOf(0.0);
        for (double worth : doubleWorthList) {
            BigDecimal value = BigDecimal.valueOf(worth);
            tempMaxValue = Decimal.max(tempMaxValue, value);
            BigDecimal rate = value.divide(tempMaxValue, 4, RoundingMode.HALF_UP).subtract(BigDecimal.valueOf(1.0));
            maxDrawDown = Decimal.min(maxDrawDown, rate);
        }
        maxDrawDown = maxDrawDown.multiply(BigDecimal.valueOf(100.0)).setScale(1, RoundingMode.HALF_UP);
        return maxDrawDown;
    }

    /**
     * 计算多时段收益
     */
    public static Map<String, Object> calculateMultiPeriodProfit(List<Map<String, Object>> worthList, Map<String, Object> currentMarket) {
        BigDecimal latestWorth = new BigDecimal((String) currentMarket.get("gsz"));

        BigDecimal history5 = (BigDecimal) worthList.get(worthList.size() - 5).get("y");
        BigDecimal day5 = Fund.getRate(latestWorth, history5);

        BigDecimal history10 = (BigDecimal) worthList.get(worthList.size() - 10).get("y");
        BigDecimal day10 = Fund.getRate(latestWorth, history10);

        BigDecimal history20 = (BigDecimal) worthList.get(worthList.size() - 20).get("y");
        BigDecimal day20 = Fund.getRate(latestWorth, history20);

        BigDecimal history60 = (BigDecimal) worthList.get(worthList.size() - 60).get("y");
        BigDecimal day60 = Fund.getRate(latestWorth, history60);

        int yearPoint = worthList.size() > 240 ? worthList.size() - 240 : 0;
        BigDecimal history240 = (BigDecimal) worthList.get(yearPoint).get("y");
        BigDecimal year = Fund.getRate(latestWorth, history240);

        //获取历史数据分析截止时间
        Calendar calendar = Calendar.getInstance();
        //6个月内最大回撤
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTime().getTime();

        worthList = worthList.stream().filter(map -> (long) map.get("x") > startTime).collect(Collectors.toList());

        BigDecimal maxDrawDown = getMaxDrawDown(worthList);

        //实时净值
        BigDecimal worth = new BigDecimal((String) currentMarket.get("gsz")).setScale(2, RoundingMode.HALF_UP);

        Map<String, Object> fundInfo = new HashMap<>();
        fundInfo.put("fundName", currentMarket.get("name"));
        fundInfo.put("fundCode", currentMarket.get("fundcode"));
        fundInfo.put("rate", currentMarket.get("gszzl"));
        fundInfo.put("worth", worth);
        fundInfo.put("day5", day5);
        fundInfo.put("day10", day10);
        fundInfo.put("day20", day20);
        fundInfo.put("day60", day60);
        fundInfo.put("year", year);
        fundInfo.put("maxDrawDown", maxDrawDown);
        return fundInfo;
    }
}