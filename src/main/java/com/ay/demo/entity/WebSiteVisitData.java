package com.ay.demo.entity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author sparkle
 *
 */
public class WebSiteVisitData {
	
	public AtomicInteger visitNum;

	private static class SingletonHolder {
		private static final WebSiteVisitData INSTANCE = new WebSiteVisitData();
	}

	private WebSiteVisitData(){}

	public static final WebSiteVisitData getInstance() {
		return SingletonHolder.INSTANCE;
	}
}
