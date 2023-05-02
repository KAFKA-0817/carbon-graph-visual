package com.kafka.carbongraphvisual.controller;


import com.kafka.carbongraphvisual.domain.ProducerDO;
import com.kafka.carbongraphvisual.meta.Result;
import com.kafka.carbongraphvisual.request.AddProducerReq;
import com.kafka.carbongraphvisual.request.UpdateProducerReq;
import com.kafka.carbongraphvisual.service.ProducerService;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Resource
    private ProducerService producerService;

    @GetMapping("/getAll")
    public Result<List<ProducerDO>> getAllProducer(){
        return Result.success(producerService.list());
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody AddProducerReq req){
        ProducerDO producerDO = BeanConvertUtil.copy(req, ProducerDO.class);
        return Result.success(producerService.save(producerDO));
    }

    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody UpdateProducerReq req){
        ProducerDO producerDO = BeanConvertUtil.copy(req, ProducerDO.class);
        return Result.success(producerService.updateById(producerDO));
    }
}
