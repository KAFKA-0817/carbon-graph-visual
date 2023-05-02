package com.kafka.carbongraphvisual.bean.DTO;

import com.kafka.carbongraphvisual.meta.Model;
import lombok.Data;

@Data
public class TransactionDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 模型
     */
    private Model model;


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
}
