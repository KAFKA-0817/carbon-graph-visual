package com.kafka.carbongraphvisual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kafka.carbongraphvisual.domain.ClientDO;
import com.kafka.carbongraphvisual.domain.mapper.ClientMapper;
import com.kafka.carbongraphvisual.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, ClientDO> implements ClientService {



    @Override
    public List<ClientDO> listByNames(List<String> names) {
        LambdaQueryWrapper<ClientDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ClientDO::getName,names);
        return baseMapper.selectList(queryWrapper);
    }
}
