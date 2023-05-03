package com.kafka.carbongraphvisual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kafka.carbongraphvisual.domain.ClientDO;

import java.util.List;

/**
 * @author Kafka
 * @since 2023-04-30
 */
public interface ClientService extends IService<ClientDO> {

    List<ClientDO> listByNames(List<String> names);
}
