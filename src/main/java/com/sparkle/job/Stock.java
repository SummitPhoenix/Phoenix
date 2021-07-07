package com.sparkle.job;

import com.alibaba.fastjson.JSON;
import com.sparkle.mapper.StockMapper;
import com.sparkle.util.Decimal;
import com.sparkle.util.HttpClientUtil;
import com.sparkle.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Smartisan
 */
@RequestMapping("/stock")
@Controller
@Slf4j
public class Stock {

    @Resource
    private StockMapper stockMapper;

    private static List<Map<String, Object>> stockList;

    private static Map<String, String> stockNames = new HashMap<>();

    private static List<String> stockInfoList;

    @GetMapping("/analyse")
    @ResponseBody
    public String getCurrentMarket() {
        return stockCheckJob();
    }

    public String stockCheckJob() {
        stockInfoList = new ArrayList<>();
        stockList = stockMapper.getStockList();
        ThreadPoolExecutor executor = ThreadPoolUtil.getThreadPoolExecutor("Stock-Analyse-%d");
        for (Map<String, Object> stock : stockList) {
            String stockCode = (String) stock.get("stockCode");
            String stockName = (String) stock.get("stockName");
            stockNames.put(stockCode, stockName);

            Stock.Task task = new Stock.Task(stockCode);
            executor.execute(task);
        }
        //关闭线程池
        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                log.info("stockInfo:{}", stockInfoList);
                return JSON.toJSONString(stockInfoList);
            }
        }
    }

    /**
     * 执行线程
     */
    class Task implements Runnable {
        private final String stockCode;

        public Task(String stockCode) {
            this.stockCode = stockCode;
        }

        @Override
        public void run() {
            analyse(stockCode);
            log.info("执行task [{}] - {}", stockCode, Thread.currentThread().getName());
        }
    }

    private void analyse(String stockCode) {
        String info = average(stockCode);
        if (StringUtils.isEmpty(info)) {
            return;
        }
        stockInfoList.add(stockNames.get(stockCode) + ":" + info);
    }

    /**
     * 今日实时行情
     */
    public Map<String, Object> getCurrentMarket(String stockCode) {
        String url = "https://stock.xueqiu.com/v5/stock/realtime/quotec.json?symbol=" + stockCode;
        String response = HttpClientUtil.sendRequest(url, null);
        Map<String, Object> json = (Map<String, Object>) JSON.parse(response);
        System.out.println(json);
        List<Map<String, Object>> data = (List<Map<String, Object>>) json.get("data");
        return data.get(0);
    }

    /**
     * 佣金 万2.5
     * 印花税 千一
     */
    private static BigDecimal calculateCommission(String type, BigDecimal money) {
        BigDecimal buyCommission = BigDecimal.valueOf(0.00025);
        BigDecimal saleCommission = BigDecimal.valueOf(0.00125);
        BigDecimal minCommission = BigDecimal.valueOf(5);
        BigDecimal commission;

        if ("买入".equals(type)) {
            commission = money.multiply(buyCommission);
        } else {
            commission = money.multiply(saleCommission);
        }
        if (Decimal.min(commission, minCommission).equals(commission)) {
            commission = minCommission;
        }
        return commission;
    }


    private String average(String stockCode) {
        String url = "https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=" + stockCode + "&begin=" + System.currentTimeMillis() + "&period=day&type=before&count=-60&indicator=kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance";
        String cookie = "device_id=a00e9a2529b25d6e61e5b52e2a621d8a; s=c211c9yqtp; bid=7ec429ae08934049eea4ec246a00f523_kmeu52gu; Hm_lvt_fe218c11eab60b6ab1b6f84fb38bcc4a=1616074929; xq_is_login=1; u=5097026868; xq_a_token=0ef79ddacfdc4eeb844875158178ac86623802bb; xqat=0ef79ddacfdc4eeb844875158178ac86623802bb; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjUwOTcwMjY4NjgsImlzcyI6InVjIiwiZXhwIjoxNjI3MDkwMTc5LCJjdG0iOjE2MjQ0OTgxNzkxNDUsImNpZCI6ImQ5ZDBuNEFadXAifQ.N-51ulOnnqktnQ9guEHHsuKlbOkExwdQ6jDvz8b5f2FFkJqdpNrfQc7pbNi8Hx1EEqju7CvjBbx_KbjTszhps_y28G3i0o4h5myl87Kn-LtgUANjjF9X0_EWcAv2xB_fF9hvIFQctWdTeDZPvioL7JfpxipEAKqbzu65IX-PYZO3I2n-jafIYRnLQRAn90rnEiAtY5PXqb3vnocSgyZMB5eLi4uLV2FDd4cnzi5ac1Gg4PKx1Bg1V-VNlMpfIuoLKoTzlkc3QvEF6YB7o0yKHrPgS72fyhPCbvMbmAPzpzi-jWmP5XmhKgDWORKnfmwLM0kL7TF-q2ixhoHusUBATg; xq_r_token=745b4ea3375e2b525aba797868afbde67d8c9c9a; Hm_lvt_1db88642e346389874251b5a1eded6e3=1624702372,1624719722,1624804932,1624842682; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1624847711";
        String response = HttpClientUtil.sendRequest(url, cookie);
        Map<String, Object> json = (Map<String, Object>) JSON.parse(response);
        Map<String, Object> data = (Map<String, Object>) json.get("data");
        List<List<BigDecimal>> item = (List<List<BigDecimal>>) data.get("item");
        //实时价格
        BigDecimal currentPrice = item.get(item.size() - 1).get(5);

        //收盘价行情数据
        List<BigDecimal> closePrices = item.stream().map(list -> list.get(5)).collect(Collectors.toList());
        Collections.reverse(closePrices);

        //计算均线
        BigDecimal ma5 = BigDecimal.valueOf(0);
        BigDecimal ma10 = BigDecimal.valueOf(0);
        BigDecimal ma20 = BigDecimal.valueOf(0);
        BigDecimal ma30 = BigDecimal.valueOf(0);
        BigDecimal ma60 = BigDecimal.valueOf(0);
        for (int i = 0; i < 60; i++) {
            switch (i) {
                case 5:
                    ma5 = ma60;
                    ma5 = ma5.divide(BigDecimal.valueOf(5), 3, RoundingMode.HALF_UP);
                    break;
                case 10:
                    ma10 = ma60;
                    ma10 = ma10.divide(BigDecimal.valueOf(10), 3, RoundingMode.HALF_UP);
                    break;
                case 20:
                    ma20 = ma60;
                    ma20 = ma20.divide(BigDecimal.valueOf(20), 3, RoundingMode.HALF_UP);
                    break;
                case 30:
                    ma30 = ma60;
                    ma30 = ma30.divide(BigDecimal.valueOf(30), 3, RoundingMode.HALF_UP);
                    break;
                default:
                    break;
            }
            ma60 = ma60.add(closePrices.get(i));
        }
        ma60 = ma60.divide(BigDecimal.valueOf(60), 3, RoundingMode.HALF_UP);

        String position = "";
        Map<BigDecimal, String> compareMap = new LinkedHashMap<>();
        compareMap.put(currentPrice, "当前");
        compareMap.put(ma5, "5日");
        compareMap.put(ma10, "10日");
        compareMap.put(ma20, "20日");
        compareMap.put(ma30, "30日");
        compareMap.put(ma60, "60日");
        compareMap = compareMap.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.reverseOrder())).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (x, y) -> {
                    throw new AssertionError();
                },
                LinkedHashMap::new
        ));
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : compareMap.values()) {
            stringBuilder.append(value);
            stringBuilder.append(",");
        }
        position = stringBuilder.toString();
        position = position.substring(0, position.length() - 1);

        Optional<Map<String, Object>> oldStockInfo = stockList.stream().filter(stock -> stockCode.equals(stock.get("stockCode"))).findFirst();
        String oldPosition = (String) oldStockInfo.get().get("position");

        int changed = 0;
        if (!position.equals(oldPosition)) {
            changed = 1;
        }

        Map<String, Object> stockInfo = new HashMap<>();
        stockInfo.put("stockCode", stockCode);
        stockInfo.put("rate", "0");
        stockInfo.put("price", currentPrice);
        stockInfo.put("ma5", ma5);
        stockInfo.put("ma10", ma10);
        stockInfo.put("ma20", ma20);
        stockInfo.put("ma30", ma30);
        stockInfo.put("ma60", ma60);
        stockInfo.put("position", position);
        stockInfo.put("changed", changed);
        stockMapper.updateStock(stockInfo);

        if (changed == 0) {
            return "";
        }

        if (Decimal.max(currentPrice, ma5).equals(currentPrice)) {
            return "5日均线上轨";
        } else if (Decimal.max(currentPrice, ma10).equals(currentPrice)) {
            return "5日均线下轨";
        } else if (Decimal.max(currentPrice, ma20).equals(currentPrice)) {
            return "10日均线下轨";
        } else if (Decimal.max(currentPrice, ma30).equals(currentPrice)) {
            return "20日均线下轨";
        } else if (Decimal.max(currentPrice, ma60).equals(currentPrice)) {
            return "30日均线下轨";
        } else {
            return "60日均线下轨";
        }
    }

}