package com.sparkle.entity;

import java.io.Serializable;

/**
 * @author sparkle
 */
public class ResponseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SUCCESS_CODE = "SUCCESS";
    private static final String FAIL_CODE = "FAIL";

    private String code;
    private String msg;
    private Object data;

    public static ResponseBean success(Object data) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(SUCCESS_CODE);
        responseBean.setData(data);
        responseBean.setMsg("");
        return responseBean;
    }

    public static ResponseBean fail(Object data, String msg) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(FAIL_CODE);
        responseBean.setData(data);
        responseBean.setMsg(msg);
        return responseBean;
    }

    public static ResponseBean fail(String msg) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(FAIL_CODE);
        responseBean.setData("");
        responseBean.setMsg(msg);
        return responseBean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
