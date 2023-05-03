package com.kafka.carbongraphvisual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kafka.carbongraphvisual.bean.VO.EdgeVO;
import com.kafka.carbongraphvisual.component.impl.CapacityWeightedEdge;
import com.kafka.carbongraphvisual.component.impl.SupplierNode;
import com.kafka.carbongraphvisual.component.impl.Vertex;
import com.kafka.carbongraphvisual.component.impl.WeightedGraph;
import com.kafka.carbongraphvisual.domain.TransactionDO;
import com.kafka.carbongraphvisual.domain.mapper.TransactionMapper;
import com.kafka.carbongraphvisual.entity.Producer;
import com.kafka.carbongraphvisual.meta.Constants;
import com.kafka.carbongraphvisual.service.TransactionService;
import com.kafka.carbongraphvisual.service.param.BatchAddNodesParam;
import com.kafka.carbongraphvisual.service.param.StartNewTransactionParam;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import com.kafka.carbongraphvisual.utils.NodeMappingUtil;
import org.jgrapht.Graph;
import org.jgrapht.alg.flow.mincost.CapacityScalingMinimumCostFlow;
import org.jgrapht.alg.flow.mincost.MinimumCostFlowProblem;
import org.jgrapht.alg.interfaces.MinimumCostFlowAlgorithm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@Service
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, TransactionDO> implements TransactionService {

    @Resource
    private TransactionMapper transactionMapper;

    @Resource
    private WeightedGraph graph;

    @Override
    public boolean startNewTransaction(StartNewTransactionParam param){
        TransactionDO currentTransaction = getEnableTransaction();
        if (currentTransaction!=null){
            //若模型为空，更名现事务
            if (graph.getVerticesSize()==0 && graph.getEdgesSize()==0) {
                currentTransaction.setTitle(param.getTitle());
                return updateById(currentTransaction);
            }

            //保存现有模型
            String modelJson = graph.getModelJson();
            currentTransaction.setModelJson(modelJson);
            currentTransaction.setStatus(1);
            updateById(currentTransaction);
            //刷新模型
            graph.restore();
        }
        //创建新事务
        TransactionDO transactionDO = BeanConvertUtil.copy(param, TransactionDO.class);
        transactionDO.setStatus(0);
        return save(transactionDO);
    }

    @Override
    public TransactionDO getCurrentTransaction() {
        //获取当前启用事务
        TransactionDO transactionDO = getEnableTransaction();
        //填充模型
        if (transactionDO!=null) transactionDO.setModelJson(graph.getModelJson());
        return transactionDO;
    }

    @Override
    public TransactionDO getEnableTransaction() {
        LambdaQueryWrapper<TransactionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TransactionDO::getStatus,0);
        return transactionMapper.selectOne(queryWrapper);
    }

    @Override
    public TransactionDO batchInsertNodes(BatchAddNodesParam param) {
        List<Vertex> graphVertices = graph.getVertices();
        graphVertices.forEach(e -> {
            e.setSupply(0);
            e.setFirstEdge(null);
        });
        graph.clearEdge();
        graphVertices.addAll(param.getNodes());
        graph.loadExt();
        //组装事务
        TransactionDO enableTransaction = getEnableTransaction();
        enableTransaction.setTotalE(0);
        enableTransaction.setTotalC(0);
        enableTransaction.setModelJson(graph.getModelJson());
        return enableTransaction;
    }

    @Override
    public TransactionDO calculateModel() {
        //图结构转换
        Graph<SupplierNode, CapacityWeightedEdge> capacityWeightedEdgeGraph = NodeMappingUtil.mapGraph(graph);
        //建立最小费用流模型
        MinimumCostFlowProblem.MinimumCostFlowProblemImpl<SupplierNode, CapacityWeightedEdge> problem = new MinimumCostFlowProblem.MinimumCostFlowProblemImpl<>(
                capacityWeightedEdgeGraph,
                SupplierNode::getSupply,
                CapacityWeightedEdge::getCapacityMax,
                CapacityWeightedEdge::getCapacityMin
        );
        //调用CapacityScaling算法
        CapacityScalingMinimumCostFlow<SupplierNode, CapacityWeightedEdge> algImpl = new CapacityScalingMinimumCostFlow<>();
        MinimumCostFlowAlgorithm.MinimumCostFlow<CapacityWeightedEdge> flow = algImpl.getMinimumCostFlow(problem);
        Map<CapacityWeightedEdge, Double> flowMap = algImpl.getFlowMap();
        Map<CapacityWeightedEdge, Double> filterMap = flowMap.entrySet().stream().filter(e -> e.getValue() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
                supplier.setSupply(supplier.getSupply()+key.getValue().intValue());
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
                producer.setSupply(producer.getSupply()+key.getValue().intValue());
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
                distributor.setSupply(distributor.getSupply()+key.getValue().intValue());
                //若为D->C边，计算epp，挂载D->C边，为C设置当前流
            }else if (source.getKey().startsWith("distributor")){
                SupplierNode target = capacityWeightedEdgeGraph.getEdgeTarget(edge);
                Vertex client = graph.getVertex(target.getKey());
                client.setSupply(client.getSupply()+key.getValue().intValue());
                Double dis = NodeMappingUtil.calculateDis(source, target);
                epp+=dis* Constants.EDC.getValue();
                graph.addEdge(new EdgeVO(source.getKey(),target.getKey(),key.getValue()));
            }

        }

        //组装事务
        TransactionDO enableTransaction = getEnableTransaction();
        enableTransaction.setModelJson(graph.getModelJson());
        enableTransaction.setTotalC(flow.getCost());
        enableTransaction.setTotalE(epp);
        return enableTransaction;
    }

    @Override
    public boolean saveCurrent() {
        TransactionDO enableTransaction = getEnableTransaction();
        if (enableTransaction!=null) {
            enableTransaction.setModelJson(graph.getModelJson());
            enableTransaction.setStatus(1);
            return updateById(enableTransaction);
        }
        return true;
    }

    @Override
    public TransactionDO loadTransaction(Long id) {
        TransactionDO transactionDO = getById(id);
        if (transactionDO == null) return null;
        transactionDO.setStatus(0);
        updateById(transactionDO);
        graph.initGraph(transactionDO);
        return transactionDO;
    }
}
