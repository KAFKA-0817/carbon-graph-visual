package com.kafka.carbongraphvisual.utils;

import java.util.HashMap;
import java.util.Map;

public class NodeMappingUtil {

    private static Map<String,String> nodeMap;

    static {
        nodeMap = new HashMap<>();
        nodeMap.put("C","Client-");
        nodeMap.put("D","Distributor-");
        nodeMap.put("P","Producer-");
        nodeMap.put("S","Supplier-");
    }

    public static String mapping(String key){
        String startWith = key.substring(0, 1);
        return nodeMap.get(startWith)+key;
    }
}
