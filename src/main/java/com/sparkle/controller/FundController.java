package com.sparkle.controller;

import com.alibaba.fastjson.JSON;
import com.sparkle.job.Fund;
import com.sparkle.mapper.mapper.FundMapper;
import com.sparkle.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

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

    private static List<Map<String, Object>> FUNDS = new CopyOnWriteArrayList<>();

    @GetMapping("/fund/fundCheckJob")
    @ResponseBody
    public String fundCheckJob() {
        fund.fundCheckJob();
        return "success";
    }

    @GetMapping("/fund")
    @ResponseBody
    public String getCurrentMarket() {
        ThreadPoolExecutor executor = ThreadPoolUtil.getThreadPoolExecutor("Fund-Sync-%d");

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
                String funds = JSON.toJSONString(FUNDS);
                FUNDS.clear();
                return funds;
            }
        }
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
            //历史数据
            List<Map<String, Object>> worthList = Fund.getHistoryData(fundCode);

            //今日实时行情
            Map<String, Object> currentMarket = Fund.getCurrentMarket(fundCode);

            //计算多时段收益
            Map<String, Object> multiPeriodProfit = Fund.calculateMultiPeriodProfit(worthList, currentMarket);
            fundMapper.updateFund(multiPeriodProfit);

            FUNDS.add(multiPeriodProfit);
            log.info("执行task [{}] - {}", fundCode, Thread.currentThread().getName());
        }

    }

}