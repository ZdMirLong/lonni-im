package com.lonni.im.core.util;

import com.lonni.im.core.action.MsgHandler;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MsgHandlerUtil：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 16:59
 */
public class MsgHandlerUtil implements Serializable {


    /**
     * 获取处理类,返回排序后的集合
     * @return
     */
    public List<Object>  getMsgHandler(){
        List<Object> objects=new ArrayList<>();

        List<Map<Object,Object>> maps=new ArrayList<>();
        Map<String, Object> beansWithAnnotation = SpringContext.getBeansWithAnnotation(MsgHandler.class);
        beansWithAnnotation.forEach((k,v)->{
            MsgHandler annotation = v.getClass().getAnnotation(MsgHandler.class);
            int order = annotation.order();
            maps.add(new ConcurrentHashMap<Object,Object>(){{
                put("order",order);
                put("bean",v);
            }});
        });
        maps.sort(Comparator.comparing(
                (Map<Object,Object>h)->Integer.parseInt(h.get("order").toString()))
        );
        maps.forEach(p->{
            objects.add(p.get("bean"));
        });

        return objects;


    }



}



