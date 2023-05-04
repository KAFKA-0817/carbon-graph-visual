package com.kafka.carbongraphvisual.bean.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SupplierVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
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
     * 坐标
     */
    private String coordinate;
}
