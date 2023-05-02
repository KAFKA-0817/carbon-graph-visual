package com.kafka.carbongraphvisual.controller;


import com.kafka.carbongraphvisual.domain.SupplierDO;
import com.kafka.carbongraphvisual.meta.Result;
import com.kafka.carbongraphvisual.request.AddSupplierReq;
import com.kafka.carbongraphvisual.request.UpdateSupplierReq;
import com.kafka.carbongraphvisual.service.SupplierService;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    private SupplierService supplierService;

    @GetMapping("/getAll")
    public Result<List<SupplierDO>> getAllSupplier(){
        return Result.success(supplierService.list());
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
}
