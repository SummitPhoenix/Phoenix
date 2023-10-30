package com.sparkle.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 东方财富板块概念涨跌幅爬虫
 * Windows系统消息通知推送
 * http://quote.eastmoney.com/center/hsbk.html
 */
@Slf4j
public class BkInfo {
    /**
     * 最高涨幅板块/概念
     */
    private static String topBK = "";
    private static String latestTopBK = "";


    public static void main(String[] args) throws Exception {
//        String fs = "m:90";
//        String mixedInfo = analyse(fs);

//        //板块概念混合
//        String fs = "m:90";
//        String mixedInfo = analyse(fs);

//        //全行业
//        fs = "m:90 t:2";
//        String bkInfo = analyse(fs);
//
//        System.out.println(mixedInfo);
//        System.out.println();
//        System.out.println(bkInfo);


//        String fs = "m:90";
//        String info = analyse(fs);
//        System.out.println(info);

        //定时任务线程池60秒触发一次更新数据
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            //交易时间结束关闭线程池
            if (now.after(StockUtil.afternoonEnd)) {
                service.shutdown();
            }
            //非交易时间不执行
            if (!StockUtil.effectiveTime(now)) {
                return;
            }
            //概念
            try {
                String fs = "m:90";
                String info = analyse(fs);
                System.out.println(info);
                if (!topBK.equals(latestTopBK)) {
                    topBK = latestTopBK;
                    StockUtil.windowsMessagePush(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 60, TimeUnit.SECONDS);

    }

    /**
     * 解析数据
     */
    private static String analyse(String fs) throws Exception {
        String url = "http://88.push2.eastmoney.com/api/qt/clist/get?";
        String originData = sendRequest(url, fs);
        originData = originData.substring(originData.indexOf("[{"), originData.lastIndexOf("}}"));
        List<Map<String, Object>> data = (List<Map<String, Object>>) JSON.parse(originData);
        latestTopBK = (String) data.get(0).get("f14") + data.get(0).get("f3").toString().charAt(0);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map<String, Object> bk : data) {
            //板块名称
            String bkName = (String) bk.get("f14");
            if (bkName.contains("昨日")) {
                continue;
            }
            //板块涨跌幅
            BigDecimal rise = ((BigDecimal) bk.get("f3"));
            //领涨股
            String stock = (String) bk.get("f128");
            //股票涨跌幅
            BigDecimal stockRise = ((BigDecimal) bk.get("f136"));
            stringBuilder.append(bkName + " " + rise + "% " + stock + " " + stockRise + "%\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 发送请求
     */
    private static String sendRequest(String url, String fs) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CloseableHttpResponse response;
            //封装请求参数
            List<BasicNameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("pn", "1"));
            list.add(new BasicNameValuePair("pz", "10"));
            list.add(new BasicNameValuePair("po", "1"));
            list.add(new BasicNameValuePair("np", "1"));
            list.add(new BasicNameValuePair("ut", "bd1d9ddb04089700cf9c27f6f7426281"));
            list.add(new BasicNameValuePair("fltt", "2"));
            list.add(new BasicNameValuePair("invt", "2"));
            list.add(new BasicNameValuePair("wbp2u", "4546386153503808|0|1|0|web"));
            list.add(new BasicNameValuePair("fid", "f3"));
            list.add(new BasicNameValuePair("fid", "f3"));
            list.add(new BasicNameValuePair("fs", fs));
            list.add(new BasicNameValuePair("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152,f133,f104,f105"));

            //3、转化参数
            String params = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));
            //4、创建HttpGet请求
            HttpGet httpGet = new HttpGet(url + params);
            //设置请求头，将爬虫伪装成浏览器
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36");
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(httpGet);
            //4.判断响应状态为200，进行处理
            //5.获取响应内容
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity, "utf-8");
        }
    }
}