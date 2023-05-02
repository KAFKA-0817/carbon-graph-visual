package com.kafka.carbongraphvisual.request;

import lombok.Data;

@Data
public class AddClientReq {

    private String name;

    private String demand;

    private String x;

    private String y;
}
