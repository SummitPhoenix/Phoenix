package com.sparkle.controller;

import com.alibaba.fastjson.JSON;
import com.sparkle.job.Fund;
import com.sparkle.util.HttpClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Smartisan
 */
@RequestMapping("/trading")
@Controller
public class TradingController {

    @GetMapping("/price/{stockCode}")
    @ResponseBody
    public Object currentPrice(@PathVariable("stockCode") String stockCode) {
        String url = "http://push2.eastmoney.com/api/qt/stock/details/get?ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1,f2,f3,f4&fields2=f51,f52,f53,f54,f55&pos=-11&secid=0." + stockCode;
        String json = HttpClientUtil.sendRequest(url);
        Map<String, Object> market = (Map<String, Object>) JSON.parse(json);
        Map<String, Object> data = (Map<String, Object>) market.get("data");
        List<String> details = (List<String>) data.get("details");

        Map<BigDecimal, Integer> prices = new HashMap<>();
        for (String info : details) {
            String[] values = info.split(",");
            BigDecimal price = new BigDecimal(values[1]);
            int count = Integer.parseInt(values[2]);
            int total = prices.get(price) == null ? 0 : prices.get(price);
            if (prices.get(price) != null) {
                prices.put(price, total + count);
            } else {
                prices.put(price, count);
            }
        }
        List<Map.Entry<BigDecimal, Integer>> priceList = prices.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        System.out.println(priceList);

        BigDecimal currentPrice = priceList.get(priceList.size() - 1).getKey();
        BigDecimal prePrice = (BigDecimal) data.get("prePrice");
        BigDecimal rate = Fund.getRate(currentPrice, prePrice);

        Map<String, Object> info = new HashMap<>();
        info.put("currentPrice", currentPrice);
        info.put("rate", rate);
        info.put("prePrice", prePrice);
        return info;
    }

}