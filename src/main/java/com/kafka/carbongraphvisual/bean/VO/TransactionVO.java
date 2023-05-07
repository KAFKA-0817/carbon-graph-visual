package com.kafka.carbongraphvisual.bean.VO;

import lombok.Data;


@Data
public class TransactionVO {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    private Long id;

    /**
     * 总碳排放量
     */
    private String totalE;

    /**
     * 总经济成本
     */
    private String totalC;

    /**
     * 事务名称
     */
    private String title;

    /**
     * 启用状态
     */
    private Integer status;
}
