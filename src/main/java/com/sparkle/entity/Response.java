package com.sparkle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sparkle
 */
@Data
@AllArgsConstructor
public class Response {

    private static final String OK = "OK";
    private static final String ERROR = "ERROR";

    private String code;
    private Object data;
    private String msg;

    public static Response success(Object data) {
        return new Response(OK, data, "");
    }

    public static Response fail(Object data, String msg) {
        return new Response(ERROR, data, msg);
    }

    public static Response fail(String msg) {
        return new Response(ERROR, "", msg);
    }
}
