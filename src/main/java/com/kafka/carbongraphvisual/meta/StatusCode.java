package com.kafka.carbongraphvisual.meta;


public enum StatusCode {

    OK(200,null),
    VERTEXNOTFOUND(10_000,"找不到结点");


    private final int code;
    private final String message;

    StatusCode(int code, String message){
        this.code=code;
        this.message=message;
    }

    int getCode(){
        return this.code;
    }

    String getMessage(){
        return this.message;
    }
}
