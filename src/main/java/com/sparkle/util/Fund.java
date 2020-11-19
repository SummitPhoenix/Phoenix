package com.sparkle.util;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author K1181378
 */
@Slf4j
@Component
public class Fund {

    /**
     * 基金列表
     * <p>
     * 易方达蓝筹精选混合 005827
     * 广发高端制造 004997
     * 招商中证白酒指数分级 161725
     * 交银品质升级混合 005004
     * 永赢消费主题 006252
     * 农银新能源主题 002190
     * 万家行业优选混合(LOF) 161903
     * 景顺长城内需成长混合 260104
     * 诺安成长混合 320007
     * 兴全趋势投资混合(LOF) 163402
     */
    private static final String[] FUND_LIST = new String[]{"005827", "004997", "161725", "005004", "006252", "002190", "161903", "260104", "320007", "163402"};

    /**
     * 邮件地址
     */
    @Value("${mailList}")
    private static String mailList;

    /**
     * 基金净值低位提醒
     */
    private static String text = "";

    private static String market = "";

    public static void main(String[] args) {
        fundCheckJob();
    }

    /**
     * 周一至周五 14:30执行
     */
    @Scheduled(cron = "* 30 14 * * 1,2,3,4,5 ")
    public static void fundCheckJob() {
        //命名线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Fund-Analyse-%d").build();
        //初始化线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5), namedThreadFactory);
        for (String fundCode : FUND_LIST) {
            Task task = new Task(fundCode);
            executor.execute(task);
        }
        //关闭线程池
        executor.shutdown();

        while (true) {
            if (executor.isTerminated()) {
                //检查提醒并发送
                if (!"".equals(text)) {
                    MailSender.sendMail("[Phoenix基金净值提醒]", text, mailList.split(","));
                    text = "";
                }
                log.info(market);
                market = "";
                break;
            }
        }

    }

    /**
     * 计算最大收益
     *
     * @param fundCode 基金代码
     * @param money    初始资金
     * @param month    月数
     */
    private static void getMaxProfit(String fundCode, int money, int month) {
        String url = "http://fund.eastmoney.com/pingzhongdata/" + fundCode + ".js";
        String js = HttpClientUtil.sendRequest(url);
        String dataNetWorthTrend = js.substring(js.indexOf("[{"), js.indexOf("}];") + 2);
        //生成数据
        List<Map<String, Object>> worthList = (List<Map<String, Object>>) JSON.parse(dataNetWorthTrend);
        BigDecimal latestWorth = (BigDecimal) worthList.get(worthList.size() - 1).get("y");

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

        min = min.setScale(2, RoundingMode.HALF_UP);
        max = max.setScale(2, RoundingMode.HALF_UP);
        String fundName = js.substring(js.indexOf("fS_name") + 11, js.indexOf("fS_code") - 6);
        String info = "\r\n";
        info += fundName + "[" + fundCode + "]\r\n";
        info += "最低: " + min + "\r\n";
        info += "最高: " + max + "\r\n";
        info += "涨幅: " + maxRate.doubleValue() + "%\r\n";
        info += "最大收益: " + maxProfit + "\r\n";
        log.info(info);
        market += info;

        //判断低位标志
        if (latestWorth.doubleValue() <= min.doubleValue()) {
            text += fundName + "(" + fundCode + ")净值处于过去" + month + "个月中最低位" + info;
        }
    }

    /**
     * 获得低位标志
     *
     * @param worthList 历史数据
     * @return 低位净值
     */
    private static BigDecimal getLowOrder(List<Map<String, Object>> worthList) {
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
        Collections.sort(lowOrders);
        double lowOrder = 0;
        for (int i = 0; i < 4; i++) {
            lowOrder += lowOrders.get(i);
        }
        lowOrder = lowOrder / 4;
        return new BigDecimal(String.valueOf(lowOrder));
    }

    /**
     * 执行线程
     */
    static class Task implements Runnable {
        private final String taskName;

        public Task(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            getMaxProfit(taskName, 10000, 3);
            log.info("执行task [{}] - {}", taskName, Thread.currentThread().getName());
        }
    }
}