package com.kafka.carbongraphvisual.request;

import lombok.Data;

@Data
public class PageQueryReq {

    private int current;

    private int size;
}
