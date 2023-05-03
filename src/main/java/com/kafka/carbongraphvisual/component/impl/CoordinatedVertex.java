package com.kafka.carbongraphvisual.component.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoordinatedVertex extends Vertex{

    private double x;
    private double y;
}
