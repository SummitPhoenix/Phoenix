package com.sparkle.controller;

import com.alibaba.fastjson.JSON;
import com.sparkle.entity.Response;
import com.sparkle.job.Fund;
import com.sparkle.mapper.FundMapper;
import com.sparkle.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Smartisan
 */

@RequestMapping("/fund")
@Controller
@Slf4j
public class FundController {

    @Resource
    private FundMapper fundMapper;

    private static final List<Map<String, Object>> FUND_LIST = new CopyOnWriteArrayList<>();

    @RequestMapping("/fundSimulate")
    public String fundSimulate() {
        return "fundSimulate";
    }

    @RequestMapping("/buy")
    @ResponseBody
    public Object buy(@RequestParam Map<String, Object> param) {
        try {
            //基金代码
            String fundCode = (String) param.get("fundCode");
            //资金
            BigDecimal money = new BigDecimal((String) param.get("money"));
            Map<String, Object> currentMarket = Fund.getCurrentMarket(fundCode);
            //持仓成本
            BigDecimal position = new BigDecimal((String) currentMarket.get("gsz"));
            String fundName = (String) currentMarket.get("name");

            //计算买入费用
            BigDecimal serviceCharge = money.multiply(Fund.BUY_RATE).setScale(4, RoundingMode.HALF_UP);
            BigDecimal rate = BigDecimal.valueOf(100).multiply(Fund.BUY_RATE);
            rate = BigDecimal.valueOf(0).subtract(rate);
            money = money.subtract(serviceCharge);
            //份额
            BigDecimal portion = money.divide(position, 4, RoundingMode.HALF_UP);
            String id = UUID.randomUUID().toString().replace("-", "");

            param.put("id", id);
            param.put("userId", "phoenix");
            param.put("fundName", fundName);
            param.put("position", position);
            param.put("worth", position);
            param.put("serviceCharge", serviceCharge);
            param.put("money", money);
            param.put("rate", rate);
            param.put("portion", portion);

            fundMapper.insertPosition(param);
        } catch (Exception e) {
            log.error("买入失败: ", e);
            return Response.fail(e, "买入失败");
        }
        return Response.success(param);
    }

    @RequestMapping("/sale")
    @ResponseBody
    public Object sale(@RequestParam Map<String, Object> param) {
        try {
            //基金代码
            String fundCode = (String) param.get("fundCode");
            Map<String, Object> currentMarket = Fund.getCurrentMarket(fundCode);
            //现价
            BigDecimal position = new BigDecimal((String) currentMarket.get("gsz"));
            //份额
            BigDecimal portion = new BigDecimal((String) param.get("portion"));
            //资金
            BigDecimal money = position.multiply(portion);
            String fundName = (String) currentMarket.get("name");

            //计算卖出费用
            BigDecimal serviceCharge = money.multiply(Fund.SALE_RATE).setScale(4, RoundingMode.HALF_UP);
            BigDecimal rate = BigDecimal.valueOf(100).multiply(Fund.BUY_RATE);
            rate = BigDecimal.valueOf(0).subtract(rate);
            money = money.subtract(serviceCharge);
            //份额
            String id = UUID.randomUUID().toString().replace("-", "");

            param.put("id", id);
            param.put("userId", "phoenix");
            param.put("fundName", fundName);
            param.put("position", position);
            param.put("worth", position);
            param.put("serviceCharge", serviceCharge);
            param.put("money", money);
            param.put("rate", rate);
            param.put("portion", portion);

            fundMapper.insertPosition(param);
        } catch (Exception e) {
            log.error("卖出失败: ", e);
            return Response.fail(e, "卖出失败");
        }
        return Response.success(param);
    }

    @GetMapping("/analyse")
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
                String funds = JSON.toJSONString(FUND_LIST);
                FUND_LIST.clear();
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

            FUND_LIST.add(multiPeriodProfit);
            log.info("执行task [{}] - {}", fundCode, Thread.currentThread().getName());
        }

    }

}