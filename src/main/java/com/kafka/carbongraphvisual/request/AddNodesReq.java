package com.kafka.carbongraphvisual.request;

import lombok.Data;

import java.util.List;

@Data
public class AddNodesReq {

    private List<String> nodes;
}
