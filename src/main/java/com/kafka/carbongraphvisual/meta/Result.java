package com.kafka.carbongraphvisual.meta;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1444605237688228650L;
    private T data;
    private boolean success;
    private int code;
    private String message;

    private Result() {
        this.success = Boolean.FALSE;
        this.code = StatusCode.OK.getCode();
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result();
        result.data((T) null).success(Boolean.TRUE);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result();
        result.data(data).success(Boolean.TRUE);
        return result;
    }

    public static <T> Result<T> error(StatusCode statusCode, Object... params) {
        Result<T> result = new Result();
        result.code(statusCode.getCode());
        String statusMessage = statusCode.getMessage();
        statusMessage = buildMessage(statusMessage, params);
        result.message(statusMessage);
        return result;
    }


    private static <T> Result<T> error(int code, String message) {
        return (new Result()).code(code).message(message);
    }

    public T getData() {
        return this.data;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    private Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> code(Integer code) {
        this.code = code;
        return this;
    }

    private Result<T> success(Boolean success) {
        this.success = success;
        return this;
    }


    public static String buildMessage(String statusMessage, Object... params) {
        StringBuilder message = new StringBuilder();
        if (null != statusMessage) {
            message.append(statusMessage);
        }

        if (params != null) {
            Object[] var4 = params;
            int var5 = params.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Object param = var4[var6];
                message.append("|").append(param);
            }
        }

        return message.toString();
    }

    public String toString() {
        return "Result{data=" + this.data + ", success=" + this.success + ", code=" + this.code + ", message='" + this.message + '\'' + '}';
    }
}
