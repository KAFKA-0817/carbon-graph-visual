package com.kafka.carbongraphvisual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kafka.carbongraphvisual.component.impl.Vertex;
import com.kafka.carbongraphvisual.component.impl.WeightedGraph;
import com.kafka.carbongraphvisual.domain.TransactionDO;
import com.kafka.carbongraphvisual.domain.mapper.TransactionMapper;
import com.kafka.carbongraphvisual.service.TransactionService;
import com.kafka.carbongraphvisual.service.param.BatchAddNodesParam;
import com.kafka.carbongraphvisual.service.param.StartNewTransactionParam;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public boolean batchInsertNodes(BatchAddNodesParam param) {
        List<Vertex> graphVertices = graph.getVertices();
        graphVertices.addAll(param.getNodes());
        return true;
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
