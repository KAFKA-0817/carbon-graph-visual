package com.kafka.carbongraphvisual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kafka.carbongraphvisual.domain.ProducerDO;
import com.kafka.carbongraphvisual.domain.mapper.ProducerMapper;
import com.kafka.carbongraphvisual.service.ProducerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@Service
public class ProducerServiceImpl extends ServiceImpl<ProducerMapper, ProducerDO> implements ProducerService {

    @Override
    public List<ProducerDO> listByNames(List<String> names) {
        LambdaQueryWrapper<ProducerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProducerDO::getName,names);
        return baseMapper.selectList(queryWrapper);
    }
}
