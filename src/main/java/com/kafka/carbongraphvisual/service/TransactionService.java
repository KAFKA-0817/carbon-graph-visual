package com.kafka.carbongraphvisual.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kafka.carbongraphvisual.domain.TransactionDO;
import com.kafka.carbongraphvisual.service.param.BatchAddNodesParam;
import com.kafka.carbongraphvisual.service.param.StartNewTransactionParam;

/**
 * @author Kafka
 * @since 2023-04-30
 */
public interface TransactionService extends IService<TransactionDO> {

    boolean startNewTransaction(StartNewTransactionParam param) throws JsonProcessingException;

    TransactionDO getCurrentTransaction();

    TransactionDO getEnableTransaction();

    boolean batchInsertNodes(BatchAddNodesParam param);

    boolean saveCurrent();

    TransactionDO loadTransaction(Long id);
}
