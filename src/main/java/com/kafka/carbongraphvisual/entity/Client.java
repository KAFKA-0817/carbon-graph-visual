package com.kafka.carbongraphvisual.entity;

import com.kafka.carbongraphvisual.component.impl.Vertex;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Client extends Vertex {

    /**
     * 客户名
     */
    private String name;

    /**
     * 客户需求量
     */
    private String demand;

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
        return "client-"+name;
    }
}
