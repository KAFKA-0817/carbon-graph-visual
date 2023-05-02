package com.kafka.carbongraphvisual.entity;

import com.kafka.carbongraphvisual.component.impl.Vertex;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Producer extends Vertex {

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
     * x坐标
     */
    private String x;

    /**
     * y坐标
     */
    private String y;

    /**
     * 单位产品制造成本
     */
    private String cpp;

    @Override
    public String getKey() {
        return "producer-"+name;
    }
}
