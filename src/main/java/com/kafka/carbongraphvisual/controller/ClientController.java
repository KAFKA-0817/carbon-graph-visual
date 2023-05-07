package com.kafka.carbongraphvisual.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kafka.carbongraphvisual.bean.VO.ClientVO;
import com.kafka.carbongraphvisual.domain.ClientDO;
import com.kafka.carbongraphvisual.meta.Result;
import com.kafka.carbongraphvisual.request.AddClientReq;
import com.kafka.carbongraphvisual.request.DeleteReq;
import com.kafka.carbongraphvisual.request.PageQueryReq;
import com.kafka.carbongraphvisual.request.UpdateClientReq;
import com.kafka.carbongraphvisual.service.ClientService;
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
@RequestMapping("/client")
public class ClientController {

    @Resource
    private ClientService clientService;

    @PostMapping("/getAll")
    public Result<Page<ClientVO>> getAllClient(@RequestBody PageQueryReq req){
        Page<ClientDO> clientDOPage = clientService.page(new Page<>(req.getCurrent(), req.getSize()));
        List<ClientVO> clientVOS = clientDOPage.getRecords().stream().map(e -> {
            ClientVO clientVO = BeanConvertUtil.copy(e, ClientVO.class);
            clientVO.setCoordinate("(" + e.getX() + "," + e.getY() + ")");
            return clientVO;
        }).collect(Collectors.toList());

        Page<ClientVO> voPage = new Page<>();
        voPage.setRecords(clientVOS);
        voPage.setCurrent(clientDOPage.getCurrent());
        voPage.setSize(clientDOPage.getSize());
        voPage.setTotal(clientDOPage.getTotal());

        return Result.success(voPage);
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

    @PostMapping("/delete")
    public Result<Boolean> deleteClient(@RequestBody DeleteReq req){
        return Result.success(clientService.removeById(req.getId()));
    }

}

