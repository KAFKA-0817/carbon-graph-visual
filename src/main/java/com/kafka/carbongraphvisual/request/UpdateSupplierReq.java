package com.kafka.carbongraphvisual.request;

import lombok.Data;

@Data
public class UpdateSupplierReq {

    private Long id;

    /**
     * 供应商名
     */
    private String name;

    /**
     * 最大供应能力
     */
    private String capacity;

    /**
     * 单位产品采购成本
     */
    private String cpp;

    /**
     * x坐标
     */
    private String x;

    /**
     * y坐标
     */
    private String y;
}
