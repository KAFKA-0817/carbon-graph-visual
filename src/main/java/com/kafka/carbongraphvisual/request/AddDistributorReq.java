package com.kafka.carbongraphvisual.request;

import lombok.Data;

@Data
public class AddDistributorReq {

    /**
     * 经销商名
     */
    private String name;

    /**
     * 最大供应能力
     */
    private String capacity;

    /**
     * x坐标
     */
    private String x;

    /**
     * y坐标
     */
    private String y;
}
