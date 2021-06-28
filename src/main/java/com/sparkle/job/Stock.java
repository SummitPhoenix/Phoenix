package com.sparkle.job;

import com.alibaba.fastjson.JSON;
import com.sparkle.util.Decimal;
import com.sparkle.util.HttpClientUtil;
import com.sparkle.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Smartisan
 */
@Slf4j
public class Stock {

    private static Map<String, String> stocks = new HashMap<>();

    static {
        stocks.put("SH512690", "酒ETF");
        stocks.put("SZ159967", "光伏产业");
        stocks.put("SH512170", "医疗ETF");
        stocks.put("SH600460", "士兰微");
        stocks.put("SH601919", "中远海控");
        stocks.put("SH515700", "新能车ETF");
        stocks.put("SH512480", "半导体ETF");
        stocks.put("SH516500", "生物科技ETF");
        stocks.put("SH159929", "医药ETF");
        stocks.put("SH165516", "信诚周期");
        stocks.put("SH515220", "煤炭ETF");
    }

    public void stockCheckJob() {
        ThreadPoolExecutor executor = ThreadPoolUtil.getThreadPoolExecutor("Stock-Analyse-%d");
        for (String stockCode : stocks.keySet()) {
            Stock.Task task = new Stock.Task(stockCode);
            executor.execute(task);
        }
        //关闭线程池
        executor.shutdown();
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
        log.info(stocks.get(stockCode) + ":" + average(stockCode));
    }

    /**
     * 今日实时行情
     */
    public static Map<String, Object> getCurrentMarket(String stockCode) {
        String url = "https://stock.xueqiu.com/v5/stock/realtime/quotec.json?symbol=" + stockCode;
        String response = HttpClientUtil.sendRequest(url, null);
        Map<String, Object> json = (Map<String, Object>) JSON.parse(response);
        System.out.println(json);
        List<Map<String, Object>> data = (List<Map<String, Object>>) json.get("data");
        return data.get(0);
    }

    public static void main(String[] args) {
        new Stock().stockCheckJob();
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


    private static String average(String stockCode) {
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