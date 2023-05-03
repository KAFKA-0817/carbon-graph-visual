package com.kafka.carbongraphvisual.entity;

import com.kafka.carbongraphvisual.component.impl.CoordinatedVertex;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Producer extends CoordinatedVertex {

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
     * 单位产品制造成本
     */
    private String cpp;

    @Override
    public String getKey() {
        return "producer-"+name;
    }
}
