package com.kafka.carbongraphvisual;

import com.kafka.carbongraphvisual.utils.NodeMappingUtil;
import org.jgrapht.alg.flow.PushRelabelMFImpl;
import org.jgrapht.alg.flow.mincost.CapacityScalingMinimumCostFlow;
import org.jgrapht.alg.flow.mincost.MinimumCostFlowProblem;
import org.jgrapht.alg.interfaces.MaximumFlowAlgorithm;
import org.jgrapht.alg.interfaces.MinimumCostFlowAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void mappingTest(){
        System.out.println(NodeMappingUtil.mapping("C03"));
    }


    @Test
    void graphTest(){
        SimpleDirectedWeightedGraph<String, CapacityWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(CapacityWeightedEdge.class);
        String  source="source",
                s1 = "S1",
                p1 = "P1", p2 = "P2",
                d1 = "D1", d2 = "D2",
                v_d1="v_d1",v_d2="v_d2",
                c = "C",
                v_c="v_C";
//        SupplierNode s1= new SupplierNode("S1",8791),
//                     p1= new SupplierNode("P1",7979),
//                     p2= new SupplierNode("P2",7435),
//                     d1= new SupplierNode("D1",4008),
//                     d2= new SupplierNode("D2",4178),
//                     c= new SupplierNode("C",-2090);


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
//        graph.setEdgeWeight(e1,1);
        CapacityWeightedEdge e2 = graph.addEdge(s1, p2);
//        graph.setEdgeWeight(e2,3);
//        CapacityWeightedEdge e3 = graph.addEdge(p1, d1);
//        graph.setEdgeWeight(e3,1);
//        CapacityWeightedEdge e4 = graph.addEdge(p1, d2);
//        graph.setEdgeWeight(e4,5);
//        CapacityWeightedEdge e5 = graph.addEdge(p2, d1);
//        graph.setEdgeWeight(e5,2);
//        CapacityWeightedEdge e6 = graph.addEdge(p2, d2);
//        graph.setEdgeWeight(e6,1);
//        CapacityWeightedEdge e7 = graph.addEdge(d1, c);
//        graph.setEdgeWeight(e7,2);
//        CapacityWeightedEdge e8 = graph.addEdge(d2, c);
//        graph.setEdgeWeight(e8,3);
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
        e11.setCapacity(0,2090);

        graph.setEdgeWeight(sourceTos1,8791);
        graph.setEdgeWeight(e1, 7979);
        graph.setEdgeWeight(e2, 7435);
        graph.setEdgeWeight(e3,(int) Double.POSITIVE_INFINITY);
        graph.setEdgeWeight(e4,(int) Double.POSITIVE_INFINITY);
        graph.setEdgeWeight(e5, (int) Double.POSITIVE_INFINITY);
        graph.setEdgeWeight(e6, (int) Double.POSITIVE_INFINITY);
        graph.setEdgeWeight(e7, 4008);
        graph.setEdgeWeight(e8, 4178);
        graph.setEdgeWeight(e9, (int) Double.POSITIVE_INFINITY);
        graph.setEdgeWeight(e10, (int) Double.POSITIVE_INFINITY);
        graph.setEdgeWeight(e11, 2090);

//        CapacityScalingMinimumCostFlow<String, CapacityWeightedEdge> costFlow = new CapacityScalingMinimumCostFlow<>();
//
//        MinimumCostFlowProblem.MinimumCostFlowProblemImpl<String, CapacityWeightedEdge> problem = new MinimumCostFlowProblem.MinimumCostFlowProblemImpl<>(
//                graph,
//                a->0,
//                CapacityWeightedEdge::getCapacityMax,
//                CapacityWeightedEdge::getCapacityMin
//        );
//        MinimumCostFlowAlgorithm.MinimumCostFlow<CapacityWeightedEdge> minimumCostFlow = costFlow.getMinimumCostFlow(problem);
//        System.out.println(costFlow.getFlowMap());
//        System.out.println(minimumCostFlow.getCost());
        PushRelabelMFImpl<String, CapacityWeightedEdge> pushRelabelMF = new PushRelabelMFImpl<>(graph);
        MaximumFlowAlgorithm.MaximumFlow<CapacityWeightedEdge> maximumFlow = pushRelabelMF.getMaximumFlow(source, c);
        System.out.println(maximumFlow.getValue());
        System.out.println(maximumFlow.getFlowMap());

    }
}
