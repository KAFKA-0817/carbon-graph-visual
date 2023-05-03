package com.kafka.carbongraphvisual.component.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierNode extends CoordinatedVertex {
    private int supply;

    @Override
    public String toString() {
        return key;
    }
}