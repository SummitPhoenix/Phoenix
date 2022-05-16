package com.sparkle.controller;

import com.sparkle.entity.Response;
import com.sparkle.job.Fund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Smartisan
 */

@RequestMapping("/stock")
@Controller
@Slf4j
public class StockController {

    @RequestMapping("/vue")
    @ResponseBody
    public Object vue(@RequestParam Map<String, Object> param) {
        String value = (String) param.get("value");
        List<String> data = new ArrayList<>();
        data.add(value);
        data.add("test");
        return data;
    }

    @RequestMapping("/buy")
    @ResponseBody
    public Object buy(@RequestParam Map<String, Object> param) {
        try {
            //证券代码 包含市场
            String stockCode = (String) param.get("stockCode");
            //数量
            String amount = (String) param.get("amount");
            //资金
            BigDecimal money = new BigDecimal((String) param.get("money"));
            Map<String, Object> currentMarket = Fund.getCurrentMarket(stockCode);
            //持仓成本
            BigDecimal position = new BigDecimal((String) currentMarket.get("gsz"));
            String fundName = (String) currentMarket.get("name");

            //计算买入费用
            BigDecimal serviceCharge = money.multiply(Fund.BUY_RATE).setScale(4, RoundingMode.HALF_UP);
            BigDecimal rate = BigDecimal.valueOf(100).multiply(Fund.BUY_RATE);
            rate = BigDecimal.valueOf(0).subtract(rate);
            BigDecimal worth = money.subtract(serviceCharge);
            //份额
            BigDecimal portion = worth.divide(position, 4, RoundingMode.HALF_UP);
            String id = UUID.randomUUID().toString().replace("-", "");

            param.put("id", id);
            param.put("userId", "phoenix");
            param.put("fundName", fundName);
            param.put("position", position);
            param.put("worth", worth);
            param.put("serviceCharge", serviceCharge);
            param.put("money", money);
            param.put("rate", rate);
            param.put("portion", portion);

//            fundMapper.insertPosition(param);
        } catch (Exception e) {
            log.error("买入失败: ", e);
            return Response.fail(e, "买入失败");
        }
        return Response.success(param);
    }
}