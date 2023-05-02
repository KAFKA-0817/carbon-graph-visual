package com.kafka.carbongraphvisual.bean.VO;

import lombok.Data;

@Data
public class EdgeVO {

    private String from;

    private String to;

    private Double distance;
}
