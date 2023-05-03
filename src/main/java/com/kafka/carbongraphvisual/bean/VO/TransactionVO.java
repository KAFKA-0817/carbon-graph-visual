package com.kafka.carbongraphvisual.bean.VO;

import lombok.Data;


@Data
public class TransactionVO {

    private static final long serialVersionUID = 1L;


    /**
     * 总碳排放量
     */
    private double totalE;

    /**
     * 总经济成本
     */
    private double totalC;

    /**
     * 事务名称
     */
    private String title;

    /**
     * 启用状态
     */
    private Integer status;
}
