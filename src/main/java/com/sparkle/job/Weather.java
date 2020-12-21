package com.sparkle.job;

import com.alibaba.fastjson.JSON;
import com.sparkle.util.HttpClientUtil;
import com.sparkle.util.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Smartisan
 */
@Slf4j
public class Weather {

    @Value("${mailList}")
    private String mailList;

    /**
     * 周一至周五 7:30执行
     */
    @Scheduled(cron = "1 30 7 * * 1,2,3,4,5")
    public void weatherWarnJob() {
        String url = "https://www.tianqiapi.com/api?version=v6&appid=91363113&appsecret=Bu02ieN7";
        String appid = "91363113";
        String appsecret = "Bu02ieN7";
        String version = "v6";
        String cityid = "101190101";
        String city = "南京";
        url += "?appid=" + appid + "&appsecret=" + appsecret + "&version=" + version + "&cityid=" + cityid + "&city=" + city;

        String json = HttpClientUtil.sendRequest(url);

        Map<String, Object> weatherInfo = (Map<String, Object>) JSON.parse(json);
        for (Map.Entry<String, Object> entry : weatherInfo.entrySet()) {
            String value = entry.getValue().toString();
            //unicode转换为utf-8
            byte[] unicodeBytes = value.getBytes(StandardCharsets.UTF_8);
            value = new String(unicodeBytes, StandardCharsets.UTF_8);
            entry.setValue(value);
        }

        log.info("WeatherWarn 天气预警: {}", weatherInfo);

        String wea = (String) weatherInfo.get("wea");
        if (!wea.contains("雨") && !wea.contains("雪")) {
            return;
        }

        String text = wea + "<br>";
        text += "气温:" + weatherInfo.get("tem") + "°    " + weatherInfo.get("tem2") + "°-" + weatherInfo.get("tem1") + "°<br>";
        text += "" + weatherInfo.get("city") + " " + weatherInfo.get("week") + " " + weatherInfo.get("update_time");
        String title = wea + " [天气提醒]";

        MailSender.sendMail(title, text, mailList.split(","));
    }

}
