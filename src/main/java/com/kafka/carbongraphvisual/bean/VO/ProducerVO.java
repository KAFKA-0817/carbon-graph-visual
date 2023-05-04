package com.kafka.carbongraphvisual.bean.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ProducerVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 制造商名
     */
    private String name;

    /**
     * 最大制造能力
     */
    private String capacity;

    /**
     * 制造单位产品的碳排放量
     */
    private String epp;

    /**
     * 坐标
     */
    private String coordinate;

    /**
     * 单位产品制造成本
     */
    private String cpp;
}
