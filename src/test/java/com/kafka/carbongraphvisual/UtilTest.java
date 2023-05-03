package com.kafka.carbongraphvisual;

import com.kafka.carbongraphvisual.domain.ClientDO;
import com.kafka.carbongraphvisual.entity.Producer;
import com.kafka.carbongraphvisual.utils.NodeMappingUtil;
import org.jgrapht.alg.flow.PushRelabelMFImpl;
import org.jgrapht.alg.flow.mincost.CapacityScalingMinimumCostFlow;
import org.jgrapht.alg.flow.mincost.MinimumCostFlowProblem;
import org.jgrapht.alg.interfaces.MaximumFlowAlgorithm;
import org.jgrapht.alg.interfaces.MinimumCostFlowAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UtilTest {

    static class CapacityWeightedEdge extends DefaultWeightedEdge{
        int capacityMax;
        int capacityMin;

        public void setCapacity(int capacityMin, int capacityMax){
            this.capacityMin=capacityMin;
            this.capacityMax=capacityMax;
        }


        public int getCapacityMax(){
            return this.capacityMax;
        }

        public int getCapacityMin(){
            return this.capacityMin;
        }
    }

    static class SupplierNode{
        String name;
        int supply;

        public SupplierNode(){}

        public SupplierNode(String name,int supply){
            this.name=name;
            this.supply=supply;
        }

        public int getSupply(){
            return this.supply;
        }

        @Override
        public String toString() {
            return name ;
        }
    }

    @Test
    void mappingTest(){
        System.out.println(NodeMappingUtil.mapping("C03"));
    }


    @Test
    void otherTest(){

    }

    @Test
    void graphTest(){
        SimpleDirectedWeightedGraph<SupplierNode, CapacityWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(CapacityWeightedEdge.class);
//        String  source="source",
//                s1 = "S1",
//                p1 = "P1", p2 = "P2",
//                d1 = "D1", d2 = "D2",
//                v_d1="v_d1",v_d2="v_d2",
//                c = "C",
//                v_c="v_C";

        SupplierNode source = new SupplierNode("source",8000),
                     s1= new SupplierNode("S1",0),
                     p1= new SupplierNode("P1",0),
                     p2= new SupplierNode("P2",0),
                     v_d1 = new SupplierNode("v_d1",0),
                     v_d2 = new SupplierNode("v_d2",0),
                     d1= new SupplierNode("D1",0),
                     d2= new SupplierNode("D2",0),
                     v_c = new SupplierNode("v_c",0),
                     c= new SupplierNode("C",-8000);


        graph.addVertex(source);
        graph.addVertex(s1);
        graph.addVertex(p1);
        graph.addVertex(p2);
        graph.addVertex(v_d1);
        graph.addVertex(v_d2);
        graph.addVertex(d1);
        graph.addVertex(d2);
        graph.addVertex(c);
        graph.addVertex(v_c);

        // 添加边
        CapacityWeightedEdge sourceTos1 = graph.addEdge(source, s1);
        CapacityWeightedEdge e1 = graph.addEdge(s1, p1);
        CapacityWeightedEdge e2 = graph.addEdge(s1, p2);
        CapacityWeightedEdge e3 = graph.addEdge(p1, v_d1);
        CapacityWeightedEdge e4 = graph.addEdge(p1, v_d2);
        CapacityWeightedEdge e5 = graph.addEdge(p2, v_d1);
        CapacityWeightedEdge e6 = graph.addEdge(p2, v_d2);
        CapacityWeightedEdge e7 = graph.addEdge(v_d1, d1);
        CapacityWeightedEdge e8 = graph.addEdge(v_d2, d2);
        CapacityWeightedEdge e9 = graph.addEdge(d1, v_c);
        CapacityWeightedEdge e10 = graph.addEdge(d2, v_c);
        CapacityWeightedEdge e11 = graph.addEdge(v_c, c);
        sourceTos1.setCapacity(0,8791);
        e1.setCapacity(0,7979);
        e2.setCapacity(0,7435);
        e3.setCapacity(0, (int) Double.POSITIVE_INFINITY);
        e4.setCapacity(0, (int) Double.POSITIVE_INFINITY);
        e5.setCapacity(0, (int) Double.POSITIVE_INFINITY);
        e6.setCapacity(0, (int) Double.POSITIVE_INFINITY);
        e7.setCapacity(0,4008);
        e8.setCapacity(0,4178);
        e9.setCapacity(0, (int) Double.POSITIVE_INFINITY);
        e10.setCapacity(0, (int) Double.POSITIVE_INFINITY);
        e11.setCapacity(0,8000);
        graph.setEdgeWeight(sourceTos1,0);
        graph.setEdgeWeight(e1,5);
        graph.setEdgeWeight(e2,7);
        graph.setEdgeWeight(e3,2);
        graph.setEdgeWeight(e4,3);
        graph.setEdgeWeight(e5,3);
        graph.setEdgeWeight(e6,6);
        graph.setEdgeWeight(e7,0);
        graph.setEdgeWeight(e8,0);
        graph.setEdgeWeight(e9,6);
        graph.setEdgeWeight(e10,3);
        graph.setEdgeWeight(e11,0);


        CapacityScalingMinimumCostFlow<SupplierNode, CapacityWeightedEdge> costFlow = new CapacityScalingMinimumCostFlow<>();

        MinimumCostFlowProblem.MinimumCostFlowProblemImpl<SupplierNode, CapacityWeightedEdge> problem = new MinimumCostFlowProblem.MinimumCostFlowProblemImpl<>(
                graph,
                SupplierNode::getSupply,
                CapacityWeightedEdge::getCapacityMax,
                CapacityWeightedEdge::getCapacityMin
        );
        MinimumCostFlowAlgorithm.MinimumCostFlow<CapacityWeightedEdge> minimumCostFlow = costFlow.getMinimumCostFlow(problem);
        System.out.println(costFlow.getFlowMap());
        System.out.println(minimumCostFlow.getCost());

    }

    @Test
    void mapTest(){
        Map<String, Object> hashMap = new HashMap<>();
        Producer producer = new Producer();
        producer.setName("P1");
        producer.setKey(producer.getKey());
        producer.setCpp("123");
        producer.setEpp("50");
        producer.setCapacity("2800");
        hashMap.put(producer.getKey(),producer);

        Producer producer1 = (Producer) hashMap.get(producer.getKey());
        System.out.println(producer1.getCpp());
    }
}
