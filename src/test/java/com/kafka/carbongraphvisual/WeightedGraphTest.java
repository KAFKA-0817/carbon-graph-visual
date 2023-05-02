package com.kafka.carbongraphvisual;


import com.kafka.carbongraphvisual.component.impl.WeightedGraph;
import org.jgrapht.alg.flow.EdmondsKarpMFImpl;
import org.jgrapht.alg.interfaces.MaximumFlowAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class WeightedGraphTest {

    @Resource
    WeightedGraph graph;

    @Test
    void Test(){
        graph.display();
    }


}
