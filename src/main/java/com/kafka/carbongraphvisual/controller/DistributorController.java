package com.kafka.carbongraphvisual.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kafka.carbongraphvisual.bean.VO.DistributorVO;
import com.kafka.carbongraphvisual.domain.DistributorDO;
import com.kafka.carbongraphvisual.meta.Result;
import com.kafka.carbongraphvisual.request.AddDistributorReq;
import com.kafka.carbongraphvisual.request.DeleteReq;
import com.kafka.carbongraphvisual.request.PageQueryReq;
import com.kafka.carbongraphvisual.request.UpdateDistributorReq;
import com.kafka.carbongraphvisual.service.DistributorService;
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
@RequestMapping("/distributor")
public class DistributorController {

    @Resource
    private DistributorService distributorService;

    @PostMapping("/getAll")
    public Result<Page<DistributorVO>> getAllDistributor(@RequestBody PageQueryReq req){
        Page<DistributorDO> distributorDOPage = distributorService.page(new Page<>(req.getCurrent(), req.getSize()));
        List<DistributorVO> distributorVOS = distributorDOPage.getRecords().stream().map(e -> {
            DistributorVO distributorVO = BeanConvertUtil.copy(e, DistributorVO.class);
            distributorVO.setCoordinate("(" + e.getX() + "," + e.getY() + ")");
            return distributorVO;
        }).collect(Collectors.toList());

        Page<DistributorVO> voPage = new Page<>();
        voPage.setRecords(distributorVOS);
        voPage.setCurrent(distributorDOPage.getCurrent());
        voPage.setSize(distributorDOPage.getSize());
        voPage.setTotal(distributorDOPage.getTotal());
        return Result.success(voPage);
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

    @PostMapping("/delete")
    public Result<Boolean> deleteDistributor(@RequestBody DeleteReq req){
        return Result.success(distributorService.removeById(req.getId()));
    }
}
