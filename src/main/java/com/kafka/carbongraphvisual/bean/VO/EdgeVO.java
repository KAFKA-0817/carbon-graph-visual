package com.kafka.carbongraphvisual.bean.VO;

import lombok.Data;

@Data
public class EdgeVO {

    public EdgeVO(){}

    public EdgeVO(String source,String target,double value){
        this.source=source;
        this.target=target;
        this.value=value;
    }

    private String source;

    private String target;

    private double value;
}
