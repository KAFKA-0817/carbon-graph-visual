package com.kafka.carbongraphvisual.utils;

import com.kafka.carbongraphvisual.component.impl.CapacityWeightedEdge;
import com.kafka.carbongraphvisual.component.impl.CoordinatedVertex;
import com.kafka.carbongraphvisual.component.impl.SupplierNode;
import com.kafka.carbongraphvisual.component.impl.WeightedGraph;
import com.kafka.carbongraphvisual.entity.Distributor;
import com.kafka.carbongraphvisual.entity.Producer;
import com.kafka.carbongraphvisual.entity.Supplier;
import com.kafka.carbongraphvisual.meta.Constants;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NodeMappingUtil {

    private static Map<String,String> nodeMap;

    static {
        nodeMap = new HashMap<>();
        nodeMap.put("C","client-");
        nodeMap.put("D","distributor-");
        nodeMap.put("P","producer-");
        nodeMap.put("S","supplier-");
    }


    public static String mapping(String key){
        String startWith = key.substring(0, 1);
        return nodeMap.get(startWith)+key;
    }

    public static Double calculateDis(CoordinatedVertex source,CoordinatedVertex target){
        return Math.sqrt(Math.pow(source.getX()-target.getX(),2)+Math.pow(source.getY()-target.getY(),2));
    }

    public static Graph<SupplierNode, CapacityWeightedEdge> mapGraph(WeightedGraph weightedGraph){
        SimpleDirectedWeightedGraph<SupplierNode, CapacityWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(CapacityWeightedEdge.class);
        HashMap<String, Object> hashMap = new HashMap<>();

        List<SupplierNode> clientNodes = weightedGraph.getClients().stream().map(e -> {
            hashMap.put(e.getKey(),e);
            SupplierNode supplierNode = BeanConvertUtil.copy(e, SupplierNode.class);
            supplierNode.setSupply(-Integer.parseInt(e.getDemand()));
            graph.addVertex(supplierNode);
            return supplierNode;
        }).collect(Collectors.toList());

        List<SupplierNode> distributorNodes = weightedGraph.getDistributors().stream().map(e -> {
            hashMap.put(e.getKey(),e);
            SupplierNode distributor = BeanConvertUtil.copy(e, SupplierNode.class);
            SupplierNode virtualDistributor = BeanConvertUtil.copy(e, SupplierNode.class);
            virtualDistributor.setKey("v-"+virtualDistributor.getKey());
            hashMap.put(virtualDistributor.getKey(),virtualDistributor);
            graph.addVertex(distributor);graph.addVertex(virtualDistributor);
            return distributor;
        }).collect(Collectors.toList());

        List<SupplierNode> producerNodes = weightedGraph.getProducers().stream().map(e -> {
            hashMap.put(e.getKey(), e);
            SupplierNode producer = BeanConvertUtil.copy(e, SupplierNode.class);
            SupplierNode virtualProducer = BeanConvertUtil.copy(e, SupplierNode.class);
            virtualProducer.setKey("v-"+virtualProducer.getKey());
            hashMap.put(virtualProducer.getKey(),virtualProducer);
            graph.addVertex(producer);graph.addVertex(virtualProducer);
            return producer;
        }).collect(Collectors.toList());

        List<SupplierNode> supplierNodes = weightedGraph.getSuppliers().stream().map(e -> {
            hashMap.put(e.getKey(), e);
            SupplierNode supplier = BeanConvertUtil.copy(e, SupplierNode.class);
            graph.addVertex(supplier);
            return supplier;
        }).collect(Collectors.toList());

        //设置超级源点汇点
        SupplierNode source = new SupplierNode(), target= new SupplierNode();
        source.setKey("Source");target.setKey("Target");target.setSupply(0);
        int totalSupply=0;
        for (SupplierNode clientNode : clientNodes) {
            totalSupply-=clientNode.getSupply();
        }
        source.setSupply(totalSupply);
        graph.addVertex(source);graph.addVertex(target);
        //建立S边
        for (SupplierNode supplierNode : supplierNodes) {
            //超级源点->S
            Supplier supplier = (Supplier) hashMap.get(supplierNode.getKey());
            CapacityWeightedEdge sourceToS = graph.addEdge(source, supplierNode);
            sourceToS.setCapacity(0,Integer.parseInt(supplier.getCapacity()));
            graph.setEdgeWeight(sourceToS,Double.parseDouble(supplier.getCpp()));
            //S->虚拟P
            for (SupplierNode producerNode : producerNodes) {
                SupplierNode virtualProducer = (SupplierNode) hashMap.get("v-" + producerNode.getKey());
                CapacityWeightedEdge sToVirtualP = graph.addEdge(supplierNode, virtualProducer);
                //设置边容量无穷大
                sToVirtualP.setCapacity(0,(int) Double.POSITIVE_INFINITY);
                //设置边费用weight=dis*(cdc+edc*factor)
                Double dis = calculateDis(supplierNode, virtualProducer);
                graph.setEdgeWeight(sToVirtualP,getDisCost(dis));
            }
        }
        //建立P边
        for (SupplierNode producerNode : producerNodes) {
            //虚拟P->P
            SupplierNode virtualProducer = (SupplierNode) hashMap.get("v-" + producerNode.getKey());
            Producer producer = (Producer) hashMap.get(producerNode.getKey());
            CapacityWeightedEdge virtualPToP = graph.addEdge(virtualProducer, producerNode);
            //设置边容量：制造商生产能力上限
            virtualPToP.setCapacity(0,Integer.parseInt(producer.getCapacity()));
            //设置边费用：制造商生产成本+碳排放处理成本
            double cost=Double.parseDouble(producer.getCpp())+Double.parseDouble(producer.getEpp())*Constants.DEFAULT_CE_FACTOR.getValue();
            graph.setEdgeWeight(virtualPToP,cost);
            //P->虚拟D
            for (SupplierNode distributorNode : distributorNodes) {
                SupplierNode virtualDistributor = (SupplierNode) hashMap.get("v-" + distributorNode.getKey());
                CapacityWeightedEdge pToVirtualD = graph.addEdge(producerNode, virtualDistributor);
                //设置边容量无穷大
                pToVirtualD.setCapacity(0,(int) Double.POSITIVE_INFINITY);
                //设置边费用weight=dis*(cdc+edc*factor)
                Double dis=calculateDis(producerNode,virtualDistributor);
                graph.setEdgeWeight(pToVirtualD,getDisCost(dis));
            }
        }
        //建立D边
        for (SupplierNode distributorNode : distributorNodes) {
            //虚拟D->D
            SupplierNode virtualDistributor = (SupplierNode) hashMap.get("v-" + distributorNode.getKey());
            Distributor distributor = (Distributor) hashMap.get(distributorNode.getKey());
            CapacityWeightedEdge virtualDToD = graph.addEdge(virtualDistributor, distributorNode);
            //设置边容量：分销商能力上限
            virtualDToD.setCapacity(0,Integer.parseInt(distributor.getCapacity()));
            //设置边费用0
            graph.setEdgeWeight(virtualDToD,0);
            //D->C
            for (SupplierNode clientNode : clientNodes) {
                CapacityWeightedEdge dToC = graph.addEdge(distributorNode, clientNode);
                //设置边容量无穷大
                dToC.setCapacity(0,(int) Double.POSITIVE_INFINITY);
                //设置边费用weight=dis*(cdc+edc*factor)
                Double dis = calculateDis(distributorNode, clientNode);
                graph.setEdgeWeight(dToC,getDisCost(dis));
            }
        }

        //建立C边
        for (SupplierNode clientNode : clientNodes) {
            //C->超级汇点
            CapacityWeightedEdge cToTarget = graph.addEdge(clientNode, target);
            cToTarget.setCapacity(0,(int) Double.POSITIVE_INFINITY);
            graph.setEdgeWeight(cToTarget,0);
        }

        return graph;
    }

    private static double getDisCost(double dis){
        return  dis * (Constants.CDC.getValue() + Constants.EDC.getValue() * Constants.DEFAULT_CE_FACTOR.getValue());
    }
}
