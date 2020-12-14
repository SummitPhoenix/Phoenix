package com.sparkle.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sparkle.job.Fund;
import com.sparkle.mapper.mapper.FundMapper;
import com.sparkle.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Smartisan
 */
@Slf4j
@RestController
public class FundController {

    @Resource
    private Fund fund;

    @Resource
    private FundMapper fundMapper;

    private static List<Map<String, Object>> FUNDS = new ArrayList<>();

    @GetMapping("/fund/fundCheckJob")
    @ResponseBody
    public String fundCheckJob() {
        fund.fundCheckJob();
        return "success";
    }

    @GetMapping("/fund")
    @ResponseBody
    public String getCurrentMarket() {
        //命名线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Fund-Sync-%d").build();
        //初始化线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5), namedThreadFactory);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        List<Map<String, Object>> fundInfoList = fundMapper.getFundList();
        List<String> fundCodeList = new ArrayList<>();
        for (Map<String, Object> fundInfo : fundInfoList) {
            String fundCode = (String) fundInfo.get("fundCode");
            fundCodeList.add(fundCode);
        }
        for (String fundCode : fundCodeList) {
            Task task = new Task(fundCode);
            executor.execute(task);
        }
        //关闭线程池
        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                for (Map<String, Object> map : FUNDS) {
                    fundMapper.updateFund(map);
                }
                String funds = JSON.toJSONString(FUNDS);
                FUNDS.clear();
                return funds;
            }
        }
    }

    /**
     * 执行线程
     */
    static class Task implements Runnable {
        private final String fundCode;

        public Task(String fundCode) {
            this.fundCode = fundCode;
        }

        @Override
        public void run() {
            String url = "http://fundgz.1234567.com.cn/js/" + fundCode + ".js";
            String json = HttpClientUtil.sendRequest(url);
            json = json.substring(json.indexOf("{"), json.indexOf("}") + 1);
            Map<String, Object> currentMarket = (Map<String, Object>) JSON.parse(json);

            url = "http://fund.eastmoney.com/pingzhongdata/" + fundCode + ".js";
            json = HttpClientUtil.sendRequest(url);
            String dataNetWorthTrend = json.substring(json.indexOf("[{"), json.indexOf("}];") + 2);
            //历史数据
            List<Map<String, Object>> worthList = (List<Map<String, Object>>) JSON.parse(dataNetWorthTrend);

            BigDecimal latestWorth = new BigDecimal((String) currentMarket.get("gsz"));

            BigDecimal history5 = (BigDecimal) worthList.get(worthList.size() - 5).get("y");
            BigDecimal day5 = getRate(latestWorth, history5);

            BigDecimal history10 = (BigDecimal) worthList.get(worthList.size() - 10).get("y");
            BigDecimal day10 = getRate(latestWorth, history10);

            BigDecimal history20 = (BigDecimal) worthList.get(worthList.size() - 20).get("y");
            BigDecimal day20 = getRate(latestWorth, history20);

            Map<String, Object> fundInfo = new HashMap<>();
            fundInfo.put("fundName", currentMarket.get("name"));
            fundInfo.put("fundCode", fundCode);
            fundInfo.put("rate", currentMarket.get("gszzl"));
            fundInfo.put("worth", currentMarket.get("gsz"));
            fundInfo.put("day5", day5);
            fundInfo.put("day10", day10);
            fundInfo.put("day20", day20);
            FUNDS.add(fundInfo);
            log.info("执行task [{}] - {}", fundCode, Thread.currentThread().getName());
        }

        private BigDecimal getRate(BigDecimal latestWorth, BigDecimal historyWorth) {
            BigDecimal rate = latestWorth.divide(historyWorth, 4, RoundingMode.HALF_UP);
            rate = rate.subtract(new BigDecimal("1"));
            rate = rate.multiply(new BigDecimal("100"));
            return rate.setScale(2, RoundingMode.HALF_UP);
        }
    }

}
