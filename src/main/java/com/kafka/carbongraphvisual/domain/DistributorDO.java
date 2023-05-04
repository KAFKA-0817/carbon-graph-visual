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
@TableName("distributor")
public class DistributorDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 经销商名
     */
    @TableField("name")
    private String name;

    /**
     * 最大供应能力
     */
    @TableField("capacity")
    private String capacity;

    /**
     * x坐标
     */
    @TableField("x")
    private double x;

    /**
     * y坐标
     */
    @TableField("y")
    private double y;


}
