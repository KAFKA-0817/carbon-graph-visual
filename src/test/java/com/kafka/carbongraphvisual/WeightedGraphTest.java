package com.kafka.carbongraphvisual;


import com.kafka.carbongraphvisual.bean.VO.EdgeVO;
import com.kafka.carbongraphvisual.component.impl.*;
import com.kafka.carbongraphvisual.entity.Producer;
import com.kafka.carbongraphvisual.meta.Constants;
import com.kafka.carbongraphvisual.service.TransactionService;
import com.kafka.carbongraphvisual.service.param.BatchAddNodesParam;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import com.kafka.carbongraphvisual.utils.NodeMappingUtil;
import org.jgrapht.Graph;
import org.jgrapht.alg.flow.EdmondsKarpMFImpl;
import org.jgrapht.alg.flow.mincost.CapacityScalingMinimumCostFlow;
import org.jgrapht.alg.flow.mincost.MinimumCostFlowProblem;
import org.jgrapht.alg.interfaces.MaximumFlowAlgorithm;
import org.jgrapht.alg.interfaces.MinimumCostFlowAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class WeightedGraphTest {

    @Resource
    WeightedGraph graph;

    @Resource
    TransactionService transactionService;

    @Test
    void Test(){
        graph.display();
    }

    @Test
    void testMap(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("C1");
        strings.add("P1");
        strings.add("D1");
        strings.add("S1");
        strings.add("D2");
        strings.add("C2");
        strings.add("C3");
        List<CoordinatedVertex> collect = strings.stream().map(e -> {
            CoordinatedVertex vertex = new CoordinatedVertex();
            vertex.setKey(NodeMappingUtil.mapping(e));
            return vertex;
        }).collect(Collectors.toList());
        BatchAddNodesParam param = new BatchAddNodesParam();
        param.setNodes(collect);
        transactionService.batchInsertNodes(param);

        Graph<SupplierNode, CapacityWeightedEdge> capacityWeightedEdgeGraph = NodeMappingUtil.mapGraph(graph);
        CapacityScalingMinimumCostFlow<SupplierNode, CapacityWeightedEdge> costFlow = new CapacityScalingMinimumCostFlow<>();

        MinimumCostFlowProblem.MinimumCostFlowProblemImpl<SupplierNode, CapacityWeightedEdge> problem = new MinimumCostFlowProblem.MinimumCostFlowProblemImpl<>(
                capacityWeightedEdgeGraph,
                SupplierNode::getSupply,
                CapacityWeightedEdge::getCapacityMax,
                CapacityWeightedEdge::getCapacityMin
        );
        MinimumCostFlowAlgorithm.MinimumCostFlow<CapacityWeightedEdge> minimumCostFlow = costFlow.getMinimumCostFlow(problem);
        Map<CapacityWeightedEdge, Double> flowMap = costFlow.getFlowMap();
        System.out.println(minimumCostFlow.getFlowMap());
        System.out.println(flowMap);
        Map<CapacityWeightedEdge, Double> filterMap = flowMap.entrySet().stream().filter(e -> e.getValue() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(filterMap);
        //计算epp,添加arc
        graph.clearEdge();
        double epp=0.0;
        for (Map.Entry<CapacityWeightedEdge, Double> key : filterMap.entrySet()) {
            CapacityWeightedEdge edge = key.getKey();
            SupplierNode source = edge.getSource();
            //若为源点->S边，为S设置当前流
            if (source.getKey().startsWith("Source")){
                SupplierNode target = capacityWeightedEdgeGraph.getEdgeTarget(edge);
                Vertex supplier = graph.getVertex(target.getKey());
                supplier.setValue(supplier.getValue()+key.getValue().intValue());
            //若为S->虚拟P边，计算epp，挂载S->P边
            }else if (source.getKey().startsWith("supplier")){
                SupplierNode target = capacityWeightedEdgeGraph.getEdgeTarget(edge);
                Double dis = NodeMappingUtil.calculateDis(source, target);
                epp+=dis* Constants.EDC.getValue();
                graph.addEdge(new EdgeVO(source.getKey(),target.getKey().substring(2),key.getValue()));
            //若为虚拟P->P边，计算epp，为P设置当前流
            }else if (source.getKey().startsWith("v-producer")){
                SupplierNode target = capacityWeightedEdgeGraph.getEdgeTarget(edge);
                Vertex producer = graph.getVertex(target.getKey());
                producer.setValue(producer.getValue()+key.getValue().intValue());
                for (Producer producerNode : graph.getProducers()) {
                    if (target.getKey().equals(producerNode.getKey())) epp+=Double.parseDouble(producerNode.getEpp())*key.getValue();
                }
            //若为P->虚拟D边，计算epp，挂载P->D边
            }else if (source.getKey().startsWith("producer")){
                SupplierNode target = capacityWeightedEdgeGraph.getEdgeTarget(edge);
                Double dis = NodeMappingUtil.calculateDis(source, target);
                epp+=dis* Constants.EDC.getValue();
                graph.addEdge(new EdgeVO(source.getKey(),target.getKey().substring(2),key.getValue()));
            //若为虚拟D->D边，为D设置当前流
            }else if (source.getKey().startsWith("v-distributor")){
                SupplierNode target = capacityWeightedEdgeGraph.getEdgeTarget(edge);
                Vertex distributor = graph.getVertex(target.getKey());
                distributor.setValue(distributor.getValue()+key.getValue().intValue());
            //若为D->C边，计算epp，挂载D->C边，为C设置当前流
            }else if (source.getKey().startsWith("distributor")){
                SupplierNode target = capacityWeightedEdgeGraph.getEdgeTarget(edge);
                Vertex client = graph.getVertex(target.getKey());
                client.setValue(client.getValue()+key.getValue().intValue());
                Double dis = NodeMappingUtil.calculateDis(source, target);
                epp+=dis* Constants.EDC.getValue();
                graph.addEdge(new EdgeVO(source.getKey(),target.getKey(),key.getValue()));
            }

        }
        System.out.println(graph.getModelJson());
        System.out.println(epp);
        System.out.println(minimumCostFlow.getCost());
    }


}
