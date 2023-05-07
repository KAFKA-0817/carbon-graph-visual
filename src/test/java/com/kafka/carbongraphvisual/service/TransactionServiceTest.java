package com.kafka.carbongraphvisual.service;

import com.kafka.carbongraphvisual.component.impl.Vertex;
import com.kafka.carbongraphvisual.entity.Client;
import com.kafka.carbongraphvisual.entity.Distributor;
import com.kafka.carbongraphvisual.entity.Producer;
import com.kafka.carbongraphvisual.entity.Supplier;
import com.kafka.carbongraphvisual.service.param.BatchAddNodesParam;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TransactionServiceTest {

    @Resource
    private TransactionService transactionService;

    @Test
    void insertTest(){
        Client client = new Client();
        client.setName("C1");
        Distributor distributor = new Distributor();
        distributor.setName("D1");
        Producer producer = new Producer();
        producer.setName("P1");
        Supplier supplier = new Supplier();
        supplier.setName("S1");

        BatchAddNodesParam param = new BatchAddNodesParam();
        List<Vertex> nodes = new ArrayList<>();
        nodes.add(client);
        nodes.add(distributor);
        nodes.add(producer);
        nodes.add(supplier);
//        param.setNodes(nodes);
        transactionService.batchInsertNodes(param);
    }
}
