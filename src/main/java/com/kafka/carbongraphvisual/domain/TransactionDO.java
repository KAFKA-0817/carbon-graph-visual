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
 *
 * @author Kafka
 * @since 2023-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("transaction")
public class TransactionDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模型json
     */
    @TableField("model_json")
    private String modelJson;

    /**
     * 总碳排放量
     */
    @TableField("total_e")
    private double totalE;

    /**
     * 总经济成本
     */
    @TableField("total_c")
    private double totalC;

    /**
     * 事务名称
     */
    @TableField("title")
    private String title;

    /**
     * 启用状态
     */
    @TableField("status")
    private Integer status;

}
