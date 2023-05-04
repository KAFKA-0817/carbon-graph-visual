package com.kafka.carbongraphvisual.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kafka.carbongraphvisual.bean.VO.ProducerVO;
import com.kafka.carbongraphvisual.domain.ProducerDO;
import com.kafka.carbongraphvisual.meta.Result;
import com.kafka.carbongraphvisual.request.AddProducerReq;
import com.kafka.carbongraphvisual.request.DeleteReq;
import com.kafka.carbongraphvisual.request.PageQueryReq;
import com.kafka.carbongraphvisual.request.UpdateProducerReq;
import com.kafka.carbongraphvisual.service.ProducerService;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Resource
    private ProducerService producerService;

    @PostMapping("/getAll")
    public Result<Page<ProducerVO>> getAllProducer(@RequestBody PageQueryReq req){
        Page<ProducerDO> producerDOPage = producerService.page(new Page<>(req.getCurrent(), req.getSize()));
        List<ProducerVO> producerVOS = producerDOPage.getRecords().stream().map(e -> {
            ProducerVO producerVO = BeanConvertUtil.copy(e, ProducerVO.class);
            producerVO.setCoordinate("(" + e.getX() + "," + e.getY() + ")");
            return producerVO;
        }).collect(Collectors.toList());

        Page<ProducerVO> voPage = new Page<>();
        voPage.setRecords(producerVOS);
        voPage.setCurrent(producerDOPage.getCurrent());
        voPage.setSize(producerDOPage.getSize());
        voPage.setTotal(producerDOPage.getTotal());
        return Result.success(voPage);
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

    @PostMapping("/delete")
    public Result<Boolean> deleteProducer(@RequestBody DeleteReq req){
        return Result.success(producerService.removeById(req.getId()));
    }
}
