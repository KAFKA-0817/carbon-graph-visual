package com.kafka.carbongraphvisual.component.impl;

import lombok.EqualsAndHashCode;
import org.jgrapht.graph.DefaultWeightedEdge;

@EqualsAndHashCode(callSuper = true)
public class CapacityWeightedEdge extends DefaultWeightedEdge {
    int capacityMax;
    int capacityMin;

    public void setCapacity(int capacityMin, int capacityMax){
        this.capacityMin=capacityMin;
        this.capacityMax=capacityMax;
    }


    public int getCapacityMax(){
        return this.capacityMax;
    }

    public int getCapacityMin(){
        return this.capacityMin;
    }

    public SupplierNode getSource(){
        return (SupplierNode) super.getSource();
    }

    public SupplierNode getTarget(){
        return (SupplierNode) super.getTarget();
    }
}