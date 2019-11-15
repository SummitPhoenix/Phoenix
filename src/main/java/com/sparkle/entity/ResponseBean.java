package com.sparkle.entity;

import java.io.Serializable;

/**
 * 
 * @author sparkle
 *
 */
public class ResponseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final String SUCCESS_CODE = "SUCCESS";
	private static final String FAIL_CODE = "FAIL";
	
	private String code;
	private Object data;
	private String error;
	
	public static ResponseBean success(Object data) {
		ResponseBean responseBean = new ResponseBean();
		responseBean.setCode(SUCCESS_CODE);
		responseBean.setData(data);
		responseBean.setError("");
		return responseBean;
	}
	
	public static ResponseBean fail(Object data, String error) {
		ResponseBean responseBean = new ResponseBean();
		responseBean.setCode(FAIL_CODE);
		responseBean.setData(data);
		responseBean.setError(error);
		return responseBean;
	}
	
	public static ResponseBean fail(String error) {
		ResponseBean responseBean = new ResponseBean();
		responseBean.setCode(FAIL_CODE);
		responseBean.setData("");
		responseBean.setError(error);
		return responseBean;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
