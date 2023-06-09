package com.kafka.carbongraphvisual.service.param;

import com.kafka.carbongraphvisual.component.impl.CoordinatedVertex;
import com.kafka.carbongraphvisual.component.impl.Vertex;
import lombok.Data;

import java.util.List;

@Data
public class BatchAddNodesParam {

    private List<CoordinatedVertex> nodes;
}
