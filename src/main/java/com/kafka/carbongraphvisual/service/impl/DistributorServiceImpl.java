package com.kafka.carbongraphvisual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kafka.carbongraphvisual.domain.DistributorDO;
import com.kafka.carbongraphvisual.domain.mapper.DistributorMapper;
import com.kafka.carbongraphvisual.service.DistributorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@Service
public class DistributorServiceImpl extends ServiceImpl<DistributorMapper, DistributorDO> implements DistributorService {

    @Override
    public List<DistributorDO> listByNames(List<String> name) {
        LambdaQueryWrapper<DistributorDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DistributorDO::getName,name);
        return baseMapper.selectList(queryWrapper);
    }
}
