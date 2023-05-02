package com.kafka.carbongraphvisual.component.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.carbongraphvisual.bean.VO.EdgeVO;
import com.kafka.carbongraphvisual.bean.VO.VertexVO;
import com.kafka.carbongraphvisual.component.Graph;
import com.kafka.carbongraphvisual.domain.TransactionDO;
import com.kafka.carbongraphvisual.domain.mapper.TransactionMapper;
import com.kafka.carbongraphvisual.meta.Model;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class WeightedGraph implements Graph {

    private TransactionMapper transactionMapper;

    private List<Vertex> vertices;
    private List<Edge> edges;

    @Autowired
    public WeightedGraph(TransactionMapper transactionMapper){
        this.transactionMapper=transactionMapper;
        //初始化顶点表边表
        this.vertices = new ArrayList<>();
        this.edges= new ArrayList<>();
        //加载当前启用事务
        LambdaQueryWrapper<TransactionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TransactionDO::getStatus,0);
        TransactionDO enableDO = transactionMapper.selectOne(queryWrapper);
        //初始化图
        initGraph(enableDO);
        display();
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
            VertexVO vertexVO = new VertexVO();
            vertexVO.setKey(vertex.getKey());
            vertices.add(vertexVO);
            Edge arc = vertex.getFirstEdge();
            while (arc!=null){
                EdgeVO edgeVO = new EdgeVO();
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
    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    @Override
    public void addEdge(EdgeVO edgeVO) {
        Edge edge = new Edge();
        edge.setTargetKey(edgeVO.getTo());
        edge.setWeight(edgeVO.getDistance());

        for (Vertex vertex : vertices) {
            if (edgeVO.getFrom().equals(vertex.getKey())){
                linkArc(vertex,edge);
                edges.add(edge);
            }
        }
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
