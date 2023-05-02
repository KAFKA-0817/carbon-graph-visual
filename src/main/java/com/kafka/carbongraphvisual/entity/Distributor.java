package com.kafka.carbongraphvisual.entity;

import com.kafka.carbongraphvisual.component.impl.Vertex;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Distributor extends Vertex {

    /**
     * 经销商名
     */
    private String name;

    /**
     * 最大供应能力
     */
    private String capacity;

    /**
     * x坐标
     */
    private String x;

    /**
     * y坐标
     */
    private String y;

    @Override
    public String getKey() {
        return "distributor-"+name;
    }
}
