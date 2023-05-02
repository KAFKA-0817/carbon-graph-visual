package com.kafka.carbongraphvisual.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.carbongraphvisual.bean.VO.TransactionModelVO;
import com.kafka.carbongraphvisual.bean.VO.TransactionVO;
import com.kafka.carbongraphvisual.component.impl.Vertex;
import com.kafka.carbongraphvisual.domain.TransactionDO;
import com.kafka.carbongraphvisual.meta.Model;
import com.kafka.carbongraphvisual.meta.Result;
import com.kafka.carbongraphvisual.request.AddNodesReq;
import com.kafka.carbongraphvisual.request.ChangeTransactionReq;
import com.kafka.carbongraphvisual.request.CreateTransactionReq;
import com.kafka.carbongraphvisual.service.TransactionService;
import com.kafka.carbongraphvisual.service.param.BatchAddNodesParam;
import com.kafka.carbongraphvisual.service.param.StartNewTransactionParam;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import com.kafka.carbongraphvisual.utils.NodeMappingUtil;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Resource
    private TransactionService transactionService;

    @GetMapping("/getAll")
    public Result<List<TransactionVO>> getAllTransaction(){
        List<TransactionDO> list = transactionService.list();
        List<TransactionVO> transactionVOList = BeanConvertUtil.copyList(list, TransactionVO.class);
        return Result.success(transactionVOList);
    }

    @PostMapping("/startNew")
    public Result<Boolean> startNewTransaction(@RequestBody CreateTransactionReq req) throws JsonProcessingException {
        StartNewTransactionParam param = BeanConvertUtil.copy(req, StartNewTransactionParam.class);
        boolean result = transactionService.startNewTransaction(param);
        return Result.success(result);
    }

    @PostMapping("/shutdown")
    public Result<Boolean> shutdownTransaction(){
        return Result.success(transactionService.saveCurrent());
    }

    @PostMapping("/change")
    public Result<TransactionModelVO> changeTransaction(@RequestBody ChangeTransactionReq req){
        transactionService.saveCurrent();
        TransactionDO transactionDO = transactionService.loadTransaction(req.getTransactionId());
        TransactionModelVO transactionModelVO = convertToModelVO(transactionDO);
        return Result.success(transactionModelVO);
    }

    @PostMapping("/addNodes")
    public Result<Boolean> addNodes(@RequestBody AddNodesReq req){
        BatchAddNodesParam param = BeanConvertUtil.copy(req, BatchAddNodesParam.class);
        List<Vertex> vertices = req.getKeys().stream().map(e -> {
            Vertex vertex = new Vertex();
            vertex.setKey(NodeMappingUtil.mapping(e));
            return vertex;
        }).collect(Collectors.toList());

        param.setNodes(vertices);
        return Result.success(transactionService.batchInsertNodes(param));
    }

    @GetMapping("/getCurrentTransaction")
    public Result<TransactionModelVO> getCurrentTransaction(){
        TransactionDO currentTransactionDO = transactionService.getCurrentTransaction();
        TransactionModelVO transactionModelVO = convertToModelVO(currentTransactionDO);
        return Result.success(transactionModelVO);
    }

    private TransactionModelVO convertToModelVO(TransactionDO transactionDO){
        if (transactionDO!=null){
            TransactionModelVO currentTransactionVO = BeanConvertUtil.copy(transactionDO, TransactionModelVO.class);
            Model model = null;
            try {
                model = new ObjectMapper().readValue(transactionDO.getModelJson(), Model.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (model!=null) {
                currentTransactionVO.setVertices(model.getVertices());
                currentTransactionVO.setEdges(model.getEdges());
            }
            return currentTransactionVO;
        }
        return null;
    }

}
