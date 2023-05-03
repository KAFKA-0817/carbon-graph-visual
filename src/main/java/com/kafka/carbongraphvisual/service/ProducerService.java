package com.kafka.carbongraphvisual.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kafka.carbongraphvisual.domain.ProducerDO;

import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
public interface ProducerService extends IService<ProducerDO> {

    List<ProducerDO> listByNames(List<String> names);

}
