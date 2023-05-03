package com.kafka.carbongraphvisual.component.impl;

import lombok.Data;

@Data
public class Edge {

    private String targetKey;

    private Edge nextEdge;

    private Double weight;
}
