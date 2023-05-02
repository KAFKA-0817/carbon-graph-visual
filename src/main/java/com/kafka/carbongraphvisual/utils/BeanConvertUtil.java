package com.kafka.carbongraphvisual.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanConvertUtil {

    public static <SOURCE,T>T copy(SOURCE source,Class<T> targetClass){
        T targetObject = BeanUtils.instantiateClass(targetClass);
        BeanUtils.copyProperties(source,targetObject);
        return targetObject;
    }

    public static <SOURCE,T> List<T> copyList(List<SOURCE> sourceList,Class<T> targetClass){
        return sourceList.stream().map(e->{
            T targetObject = BeanUtils.instantiateClass(targetClass);
            BeanUtils.copyProperties(e, targetObject);
            return targetObject;
        }).collect(Collectors.toList());
    }

}
