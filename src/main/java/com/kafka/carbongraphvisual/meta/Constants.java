package com.kafka.carbongraphvisual.meta;

public enum Constants {

    CDC(4.35),
    EDC(1.25),
    DEFAULT_CE_FACTOR(0.25);

    private final double value;

    Constants(double value){
        this.value=value;
    }

    public double getValue(){
        return this.value;
    }
}
