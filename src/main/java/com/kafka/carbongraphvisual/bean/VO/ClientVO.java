package com.kafka.carbongraphvisual.bean.VO;

import lombok.Data;

@Data
public class ClientVO {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 客户名
     */
    private String name;

    /**
     * 客户需求量
     */
    private String demand;

    /**
     * 客户坐标
     */
    private String coordinate;
}
