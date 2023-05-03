package com.kafka.carbongraphvisual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kafka.carbongraphvisual.domain.DistributorDO;

import java.util.List;

/**
 * <p>
 * 经销商表 服务类
 * </p>
 *
 * @author Kafka
 * @since 2023-04-30
 */
public interface DistributorService extends IService<DistributorDO> {

    List<DistributorDO> listByNames(List<String> name);

}
