package com.kafka.carbongraphvisual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kafka.carbongraphvisual.domain.SupplierDO;
import com.kafka.carbongraphvisual.domain.mapper.SupplierMapper;
import com.kafka.carbongraphvisual.service.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, SupplierDO> implements SupplierService {

    @Override
    public List<SupplierDO> listByNames(List<String> names) {
        LambdaQueryWrapper<SupplierDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SupplierDO::getName,names);
        return baseMapper.selectList(queryWrapper);
    }
}
