package com.kafka.carbongraphvisual.controller;


import com.kafka.carbongraphvisual.domain.ClientDO;
import com.kafka.carbongraphvisual.meta.Result;
import com.kafka.carbongraphvisual.request.AddClientReq;
import com.kafka.carbongraphvisual.request.DeleteClientReq;
import com.kafka.carbongraphvisual.request.UpdateClientReq;
import com.kafka.carbongraphvisual.service.ClientService;
import com.kafka.carbongraphvisual.utils.BeanConvertUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    @Resource
    private ClientService clientService;

    @GetMapping("/getAll")
    public Result<List<ClientDO>> getAllClient(){
        return Result.success(clientService.list());
    }

    @PostMapping("/add")
    public Result<Boolean> addClient(@RequestBody AddClientReq req){
        ClientDO clientDO = BeanConvertUtil.copy(req, ClientDO.class);
        return Result.success(clientService.save(clientDO));
    }

    @PostMapping("/update")
    public Result<Boolean> updateClient(@RequestBody UpdateClientReq req){
        ClientDO clientDO = BeanConvertUtil.copy(req, ClientDO.class);
        return Result.success(clientService.updateById(clientDO));
    }
}

