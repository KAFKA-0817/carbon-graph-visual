package com.kafka.carbongraphvisual.component;


import com.kafka.carbongraphvisual.bean.VO.EdgeVO;
import com.kafka.carbongraphvisual.component.impl.Vertex;

public interface Graph {

    int getVerticesSize();
    int getEdgesSize();

    Vertex getVertex(String key);

    void addVertex(Vertex vertex);
    void addEdge(EdgeVO edgeVO);

    void clearEdge();
    void removeVertex(Vertex v);
    void removeEdge(EdgeVO edgeVO);

    void display();
}
