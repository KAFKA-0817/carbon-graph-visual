package com.kafka.carbongraphvisual.bean.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class DistributorVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 经销商名
     */
    private String name;

    /**
     * 最大供应能力
     */
    private String capacity;

    /**
     * 坐标
     */
    private String coordinate;
}
