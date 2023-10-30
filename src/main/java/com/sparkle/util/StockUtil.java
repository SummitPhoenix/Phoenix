package com.sparkle.util;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class StockUtil {
    /**
     * 上午开盘时间
     */
    private static final Calendar morningStart = Calendar.getInstance();
    /**
     * 上午闭市时间
     */
    private static final Calendar morningEnd = Calendar.getInstance();
    /**
     * 下午开盘时间
     */
    private static final Calendar afternoonStart = Calendar.getInstance();
    /**
     * 下午闭市时间
     */
    public static final Calendar afternoonEnd = Calendar.getInstance();

    static {
        Date nowDate = new Date();
        //9:30-11:30
        morningStart.setTime(nowDate);
        morningStart.set(Calendar.HOUR_OF_DAY, 9);
        morningStart.set(Calendar.MINUTE, 30);
        morningStart.set(Calendar.SECOND, 0);

        morningEnd.setTime(nowDate);
        morningEnd.set(Calendar.HOUR_OF_DAY, 11);
        morningEnd.set(Calendar.MINUTE, 30);
        morningEnd.set(Calendar.SECOND, 0);
        //13:00-14:57
        afternoonStart.setTime(nowDate);
        afternoonStart.set(Calendar.HOUR_OF_DAY, 13);
        afternoonStart.set(Calendar.MINUTE, 0);
        afternoonStart.set(Calendar.SECOND, 0);

        afternoonEnd.setTime(nowDate);
        afternoonEnd.set(Calendar.HOUR_OF_DAY, 14);
        afternoonEnd.set(Calendar.MINUTE, 57);
        afternoonEnd.set(Calendar.SECOND, 0);
    }

    /**
     * 判断当前时间是否有效
     */
    public static boolean effectiveTime(Calendar now) {
        //上午交易时间
        boolean morningEffective = isEffectiveDate(now, morningStart, morningEnd);
        if (morningEffective) {
            return true;
        }
        //下午交易时间
        boolean afternoonEffective = isEffectiveDate(now, afternoonStart, afternoonEnd);
        if (afternoonEffective) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前时间在时间区间内
     *
     * @param now   当前时间
     * @param start 开始时间
     * @param end   结束时间
     */
    private static boolean isEffectiveDate(Calendar now, Calendar start, Calendar end) {
        if (now.getTime().equals(start.getTime()) || now.getTime().equals(end.getTime())) {
            return true;
        }
        return now.after(start) && now.before(end);
    }

    /**
     * Windows系统消息通知推送
     *
     * @param text 通知推送内容
     */
    public static void windowsMessagePush(String text) throws Exception {
        SystemTray systemTray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        systemTray.add(trayIcon);
        //caption为空标题扩大正文行数
        trayIcon.displayMessage("", text, TrayIcon.MessageType.INFO);
    }
}