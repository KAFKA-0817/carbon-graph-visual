package com.kafka.carbongraphvisual.bean.VO;

import lombok.Data;

@Data
public class EdgeVO {

    public EdgeVO(){}

    public EdgeVO(String from,String to,Double weight){
        this.from=from;
        this.to=to;
        this.weight=weight;
    }

    private String from;

    private String to;

    private Double weight;
}
