package com.kafka.carbongraphvisual.component.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.carbongraphvisual.bean.VO.EdgeVO;
import com.kafka.carbongraphvisual.bean.VO.VertexVO;
import com.kafka.carbongraphvisual.component.Graph;
import com.kafka.carbongraphvisual.domain.ClientDO;
import com.kafka.carbongraphvisual.domain.TransactionDO;
import com.kafka.carbongraphvisual.domain.mapper.TransactionMapper;
import com.kafka.carbongraphvisual.entity.Client;
import com.kafka.carbongraphvisual.entity.Distributor;
import com.kafka.carbongraphvisual.entity.Producer;
import com.kafka.carbongraphvisual.entity.Supplier;
import com.kafka.carbongraphvisual.meta.Model;
import com.kafka.carbongraphvisual.service.ClientService;
import com.kafka.carbongraphvisual.service.DistributorService;
import com.kafka.carbongraphvisual.service.ProducerService;
import com.kafka.carbongraphvisual.service.SupplierService;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class WeightedGraph implements Graph {

    private TransactionMapper transactionMapper;
    private ClientService clientService;
    private DistributorService distributorService;
    private ProducerService producerService;
    private SupplierService supplierService;

    private List<Vertex> vertices;
    private List<Edge> edges;
    private List<Client> clients;
    private List<Distributor> distributors;
    private List<Producer> producers;
    private List<Supplier> suppliers;

    @Autowired
    public WeightedGraph(TransactionMapper transactionMapper,
                         ClientService clientService,
                         DistributorService distributorService,
                         ProducerService producerService,
                         SupplierService supplierService){
        this.transactionMapper=transactionMapper;
        this.clientService=clientService;
        this.distributorService=distributorService;
        this.producerService=producerService;
        this.supplierService=supplierService;

        //初始化顶点表边表
        this.vertices = new ArrayList<>();
        this.edges= new ArrayList<>();
        //加载当前启用事务
        LambdaQueryWrapper<TransactionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TransactionDO::getStatus,0);
        TransactionDO enableDO = transactionMapper.selectOne(queryWrapper);
        //初始化图
        initGraph(enableDO);
        //加载结点详细属性
        loadExt();
        display();
    }

    public void loadExt(){
        ArrayList<String> clientNames = new ArrayList<>();
        ArrayList<String> distributorNames = new ArrayList<>();
        ArrayList<String> producerNames = new ArrayList<>();
        ArrayList<String> supplierNames = new ArrayList<>();
        vertices.forEach(e ->{
            String[] split = e.getKey().split("-");
            switch (split[0]){
                case "client": clientNames.add(split[1]);break;
                case "distributor": distributorNames.add(split[1]);break;
                case "producer": producerNames.add(split[1]);break;
                case "supplier": supplierNames.add(split[1]);break;
            }
        });

        if (!clientNames.isEmpty()) {
            List<ClientDO> clientDOS = clientService.listByNames(clientNames);
            this.clients = BeanConvertUtil.copyList(clientDOS, Client.class);
        }

        if (!distributorNames.isEmpty()) this.distributors=BeanConvertUtil.copyList(distributorService.listByNames(distributorNames),Distributor.class);
        if (!producerNames.isEmpty()) this.producers=BeanConvertUtil.copyList(producerService.listByNames(producerNames),Producer.class);
        if (!supplierNames.isEmpty()) this.suppliers=BeanConvertUtil.copyList(supplierService.listByNames(supplierNames),Supplier.class);
    }

    public void initGraph(TransactionDO transaction){
        //反序列化模型
        Model model = null;
        if (transaction!=null && transaction.getModelJson()!=null){
            try {
                model = new ObjectMapper().readValue(transaction.getModelJson(), Model.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        //边表挂载
        if (model!=null){
            this.vertices = model.getVertices().stream().map(e -> {
                Vertex vertex = new Vertex();
                vertex.setKey(e.getKey());
                return vertex;
            }).collect(Collectors.toList());
            model.getEdges().forEach(this::addEdge);
        }
        System.out.println("Graph init");
    }

    public String getModelJson(){
        Model model = new Model();
        List<VertexVO> vertices= new ArrayList<>();
        List<EdgeVO> edgeVOS=new ArrayList<>();
        for (Vertex vertex : this.vertices) {
            VertexVO vertexVO = BeanConvertUtil.copy(vertex,VertexVO.class);
            vertices.add(vertexVO);
            Edge arc = vertex.getFirstEdge();
            while (arc!=null){
                EdgeVO edgeVO = BeanConvertUtil.copy(arc,EdgeVO.class);
                edgeVO.setFrom(vertex.getKey());
                edgeVO.setTo(arc.getTargetKey());
                edgeVOS.add(edgeVO);
                arc=arc.getNextEdge();
            }
        }
        model.setEdges(edgeVOS);
        model.setVertices(vertices);
        try {
            return new ObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void restore(){
        vertices=new ArrayList<>();
        edges=new ArrayList<>();
    }

    private void linkArc(Vertex vertex, Edge edge){
        if (vertex.getFirstEdge() == null) {
            vertex.setFirstEdge(edge);
        }else {
            Edge arc = vertex.getFirstEdge();
            while (arc.getNextEdge()!=null) arc=arc.getNextEdge();
            arc.setNextEdge(edge);
        }
    }

    @Override
    public int getVerticesSize() {
        return vertices.size();
    }

    @Override
    public int getEdgesSize() {
        return edges.size();
    }

    @Override
    public Vertex getVertex(String key) {
        for (Vertex vertex : vertices) {
            if (key.equals(vertex.getKey())) return vertex;
        }
        return null;
    }

    @Override
    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    @Override
    public void addEdge(EdgeVO edgeVO) {
        Edge edge = BeanConvertUtil.copy(edgeVO, Edge.class);
        edge.setTargetKey(edgeVO.getTo());

        for (Vertex vertex : vertices) {
            if (edgeVO.getFrom().equals(vertex.getKey())){
                linkArc(vertex,edge);
                edges.add(edge);
            }
        }
    }

    @Override
    public void clearEdge() {
        this.edges=new ArrayList<>();
    }

    @Override
    public void removeVertex(Vertex v) {

    }

    @Override
    public void removeEdge(EdgeVO edgeVO) {

    }

    @Override
    public void display() {
        System.out.println("Graph display :");
        vertices.forEach(e -> {
            System.out.print(e.getKey());
            Edge arc = e.getFirstEdge();
            while (arc!=null){
                System.out.print("->"+arc.getTargetKey());
                arc=arc.getNextEdge();
            }
            System.out.println();
        });
    }
}
