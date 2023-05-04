package com.kafka.carbongraphvisual.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kafka.carbongraphvisual.bean.VO.SupplierVO;
import com.kafka.carbongraphvisual.domain.SupplierDO;
import com.kafka.carbongraphvisual.meta.Result;
import com.kafka.carbongraphvisual.request.AddSupplierReq;
import com.kafka.carbongraphvisual.request.DeleteReq;
import com.kafka.carbongraphvisual.request.PageQueryReq;
import com.kafka.carbongraphvisual.request.UpdateSupplierReq;
import com.kafka.carbongraphvisual.service.SupplierService;
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
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    private SupplierService supplierService;

    @PostMapping("/getAll")
    public Result<Page<SupplierVO>> getAllSupplier(@RequestBody PageQueryReq req){
        Page<SupplierDO> doPage = supplierService.page(new Page<>(req.getCurrent(),req.getSize()));
        List<SupplierVO> supplierVOS = doPage.getRecords().stream().map(e -> {
            SupplierVO supplierVO = BeanConvertUtil.copy(e, SupplierVO.class);
            supplierVO.setCoordinate("(" + e.getX() + "," + e.getY() + ")");
            return supplierVO;
        }).collect(Collectors.toList());

        Page<SupplierVO> voPage = new Page<>();
        voPage.setCurrent(doPage.getCurrent());
        voPage.setRecords(supplierVOS);
        voPage.setSize(doPage.getSize());
        voPage.setTotal(doPage.getTotal());


        return Result.success(voPage);
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody AddSupplierReq req){
        SupplierDO supplierDO = BeanConvertUtil.copy(req, SupplierDO.class);
        return Result.success(supplierService.save(supplierDO));
    }

    @PostMapping("/update")
    public Result<Boolean> add(@RequestBody UpdateSupplierReq req){
        SupplierDO supplierDO = BeanConvertUtil.copy(req, SupplierDO.class);
        return Result.success(supplierService.updateById(supplierDO));
    }

    @PostMapping("/delete")
    public Result<Boolean> deleteSupplier(@RequestBody DeleteReq req){
        return Result.success(supplierService.removeById(req.getId()));
    }
}
