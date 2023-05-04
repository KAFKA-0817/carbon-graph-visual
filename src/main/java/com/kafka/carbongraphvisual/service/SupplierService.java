package com.kafka.carbongraphvisual.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kafka.carbongraphvisual.domain.SupplierDO;

import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
public interface SupplierService extends IService<SupplierDO> {

    List<SupplierDO> listByNames(List<String> names);

}
