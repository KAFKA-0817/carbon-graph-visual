package com.kafka.carbongraphvisual.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("supplier")
public class SupplierDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 供应商名
     */
    @TableField("name")
    private String name;

    /**
     * 最大供应能力
     */
    @TableField("capacity")
    private String capacity;

    /**
     * 单位产品采购成本
     */
    @TableField("cpp")
    private String cpp;

    /**
     * x坐标
     */
    @TableField("x")
    private String x;

    /**
     * y坐标
     */
    @TableField("y")
    private String y;


}
