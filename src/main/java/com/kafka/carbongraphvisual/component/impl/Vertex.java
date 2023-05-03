package com.kafka.carbongraphvisual.component.impl;

import lombok.Data;

@Data
public class Vertex {

    protected String key;

    private int supply;

    private Edge firstEdge;
}
