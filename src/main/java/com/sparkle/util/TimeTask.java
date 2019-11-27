package com.sparkle.util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author sparkle
 */
public class TimeTask {

	private static final long ONE_DAY = 24 * 60 * 60 * 1000;

	private static Robot robot;

	public static void main(String[] args) {

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				try {
					robot = new Robot();
				} catch (AWTException e) {
					e.printStackTrace();
				}
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_T);
				robot.delay(20);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_T);
				robot.delay(1000);

				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_F4);
				robot.delay(20);
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_F4);
				robot.delay(20);
			}
		};
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,19);
		calendar.set(Calendar.MINUTE,54);
		calendar.set(Calendar.SECOND,0);
		//第一次执行定时任务的时间
		Date date = calendar.getTime();
		//如果第一次执行定时任务的时间 小于当前的时间
		//此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		if(date.before(new Date())){
			date.setTime(date.getTime()+ONE_DAY);
		}

		Timer timer = new Timer();
		//安排指定的任务在指定的时间开始进行重复的固定延迟执行。

		timer.schedule(timerTask,date,ONE_DAY);
	}
}