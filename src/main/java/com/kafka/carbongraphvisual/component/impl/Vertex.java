package com.kafka.carbongraphvisual.component.impl;

import lombok.Data;

@Data
public class Vertex {

    protected String key;

    private int value;

    private Edge firstEdge;
}
