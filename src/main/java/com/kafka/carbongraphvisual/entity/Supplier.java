package com.kafka.carbongraphvisual.entity;

import com.kafka.carbongraphvisual.component.impl.Vertex;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Supplier  extends Vertex {

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
     * x坐标
     */
    private String x;

    /**
     * y坐标
     */
    private String y;

    @Override
    public String getKey() {
        return "supplier-"+name;
    }
}
