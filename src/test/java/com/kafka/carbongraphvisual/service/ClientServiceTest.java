package com.kafka.carbongraphvisual.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;

@SpringBootTest
public class ClientServiceTest {

    @Resource
    private ClientService clientService;

    @Test
    void test(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("C1");
        strings.add("C2");
        strings.add("C3");
        System.out.println(clientService.listByNames(strings));
    }
}
