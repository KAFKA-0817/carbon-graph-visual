package com.kafka.carbongraphvisual.entity;

import com.kafka.carbongraphvisual.component.impl.CoordinatedVertex;
import com.kafka.carbongraphvisual.component.impl.Vertex;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Client extends CoordinatedVertex {

    /**
     * 客户名
     */
    private String name;

    /**
     * 客户需求量
     */
    private String demand;


    @Override
    public String getKey() {
        return "client-"+name;
    }
}
