package com.kafka.carbongraphvisual.controller;


import com.kafka.carbongraphvisual.domain.DistributorDO;
import com.kafka.carbongraphvisual.meta.Result;
import com.kafka.carbongraphvisual.request.AddDistributorReq;
import com.kafka.carbongraphvisual.request.UpdateDistributorReq;
import com.kafka.carbongraphvisual.service.DistributorService;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@RestController
@RequestMapping("/distributor")
public class DistributorController {

    @Resource
    private DistributorService distributorService;

    @GetMapping("/getAll")
    public Result<List<DistributorDO>> getAllDistributor(){
        return Result.success(distributorService.list());
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody AddDistributorReq req){
        DistributorDO distributorDO = BeanConvertUtil.copy(req, DistributorDO.class);
        return Result.success(distributorService.save(distributorDO));
    }

    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody UpdateDistributorReq req){
        DistributorDO distributorDO = BeanConvertUtil.copy(req, DistributorDO.class);
        return Result.success(distributorService.updateById(distributorDO));
    }
}
