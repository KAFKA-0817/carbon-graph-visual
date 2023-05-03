package com.kafka.carbongraphvisual.entity;

import com.kafka.carbongraphvisual.component.impl.CoordinatedVertex;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Distributor extends CoordinatedVertex {

    /**
     * 经销商名
     */
    private String name;

    /**
     * 最大供应能力
     */
    private String capacity;



    @Override
    public String getKey() {
        return "distributor-"+name;
    }
}
