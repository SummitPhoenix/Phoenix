package com.sparkle.job;

import com.alibaba.fastjson.JSON;
import com.sparkle.mapper.StockMapper;
import com.sparkle.util.Decimal;
import com.sparkle.util.HttpClientUtil;
import com.sparkle.util.MailSender;
import com.sparkle.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
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

    @Value("${mailList}")
    private String mailList;

    @Resource
    private StockMapper stockMapper;

    /**
     * 证券数据
     */
    private static List<Map<String, Object>> stockList;

    /**
     * 代码:名称 字典
     */
    private static Map<String, String> stockNames = new HashMap<>();

    /**
     * 均线提醒
     */
    private static List<String> stockInfoList;

    @GetMapping("/analyse")
    @ResponseBody
    public String getCurrentMarket() {
        return stockCheckJob();
    }

    /**
     * 周一至周五 10:00 14:00执行
     */
    @Scheduled(cron = "* 40 9,10,14 * * 1,2,3,4,5")
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

                //邮件
                if (!stockInfoList.isEmpty()) {
                    StringBuilder text = new StringBuilder();
                    for (String stockInfo : stockInfoList) {
                        text.append(stockInfo);
                    }
                    String title = "[均线提醒]";
                    MailSender.sendMail(title, text.toString(), mailList.split(","));
                }
                //微信push plus
//                if (!stockInfoList.isEmpty()) {
//                    StringBuilder text = new StringBuilder();
//                    for (String stockInfo : stockInfoList) {
//                        text.append(stockInfo);
//                    }
//                    String url = "http://pushplus.hxtrip.com/send?token=cace7b6e38db41e5acb7997f4efe6122&title=均线提醒&content=&template=" + text.toString();
//                    String response = HttpClientUtil.sendRequest(url, null);
//                    log.info("push plus response:{}", response);
//                }


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
        if (info == null) {
            return;
        }
        stockInfoList.add(stockNames.get(stockCode) + "  " + info + "<br>");
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


    /**
     * 计算均线提醒
     */
    private String average(String stockCode) {
        String url = "https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=" + stockCode + "&begin=" + System.currentTimeMillis() + "&period=day&type=before&count=-60&indicator=kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance";
        String cookie = "device_id=78ef428047050ccb79132e4473ffc998; s=ci12q28n9b; bid=7ec429ae08934049eea4ec246a00f523_ksfhmi3a; Hm_lvt_fe218c11eab60b6ab1b6f84fb38bcc4a=1629251530; xq_is_login=1; u=6768671760; xq_a_token=82b3d484547fbe4e6600e8fa3e9e23f48ad580c3; xqat=82b3d484547fbe4e6600e8fa3e9e23f48ad580c3; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjY3Njg2NzE3NjAsImlzcyI6InVjIiwiZXhwIjoxNjM2MTg0OTU1LCJjdG0iOjE2MzM1OTI5NTU1NDcsImNpZCI6ImQ5ZDBuNEFadXAifQ.BGCdWG_-jea5FyiDnyYApsR7VfYg3EXOsW_r0AZ3nNh_I2IAyE1SJHKBFQaUpR5kc5Xkhn0Cenj0rK-XDEndG25T645x9fNmw-IfZ-dlhVekphWTgfdhDoqPf0OYcbHbjuM6gllL6lFLMBnd_zgOTZe5ndsJ0qe1bUldjz5CqgKTeI8Edks8QmgHfgL8s6fAILIlfH0eUlMkfF30fWjgDxaOxgPyCZGqf7ZFOxqPfbYKXeas9k6wfVo-AO_r4yqEym3zMvRfbgYOzlneLuDg1qqb8Zvke-Q9qiNa1GsaqD-yYZdW9p07ImH8DIzzHGg1xqjNiV16wr62V_d5CpRTgA; xq_r_token=c285e264246439b47a4eabf9570f100b7a356013; Hm_lvt_1db88642e346389874251b5a1eded6e3=1633004078,1633592957,1633605427,1633654506; is_overseas=0; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1633681996";
        String response = HttpClientUtil.sendRequest(url, cookie);
        if ("异常".equals(response)) {
            return null;
        }
        Map<String, Object> json = (Map<String, Object>) JSON.parse(response);
        Map<String, Object> data = (Map<String, Object>) json.get("data");
        List<List<BigDecimal>> item = (List<List<BigDecimal>>) data.get("item");
        //实时价格
        BigDecimal currentPrice = item.get(item.size() - 1).get(5);
        //涨跌幅
        BigDecimal percent = item.get(item.size() - 1).get(7);

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

        //均线位置排序
        Map<String, BigDecimal> compareMap = new LinkedHashMap<>();
        compareMap.put("当前", currentPrice);
        compareMap.put("5日", ma5);
        compareMap.put("10日", ma10);
        compareMap.put("20日", ma20);
        compareMap.put("30日", ma30);
        compareMap.put("60日", ma60);
        compareMap = compareMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (x, y) -> {
                    throw new AssertionError();
                },
                LinkedHashMap::new
        ));
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : compareMap.keySet()) {
            stringBuilder.append(key);
            stringBuilder.append(",");
        }
        String position = stringBuilder.toString();
        position = position.substring(0, position.length() - 1);

        Optional<Map<String, Object>> oldStockInfo = stockList.stream().filter(stock -> stockCode.equals(stock.get("stockCode"))).findFirst();
        String oldPosition = (String) oldStockInfo.get().get("position");
        int changed = position.indexOf("当前") == oldPosition.indexOf("当前") ? 0 : 1;

        //更新数据
        Map<String, Object> stockInfo = new HashMap<>();
        stockInfo.put("stockCode", stockCode);
        stockInfo.put("rate", percent);
        stockInfo.put("price", currentPrice);
        stockInfo.put("ma5", ma5);
        stockInfo.put("ma10", ma10);
        stockInfo.put("ma20", ma20);
        stockInfo.put("ma30", ma30);
        stockInfo.put("ma60", ma60);
        stockInfo.put("position", position);
        stockInfo.put("changed", changed);
        stockMapper.updateStock(stockInfo);

        //均线位置未改变不提醒
        if (changed == 0) {
            return null;
        }

        //判断当前均线位置
        String[] averageLines = oldPosition.split(",");
        int offset = 0;
        for (int i = 0; i < averageLines.length; i++) {
            if ("当前".equals(averageLines[i])) {
                offset = i;
            }
        }

        //生成均线位置变化提醒
        String[] currentAverageLines = position.split(",");
        String info;
        if (percent.doubleValue() <= 0) {
            info = "下跌 " + currentAverageLines[offset];
        } else {
            info = "上涨 " + currentAverageLines[offset];
        }
        return info;
    }

}