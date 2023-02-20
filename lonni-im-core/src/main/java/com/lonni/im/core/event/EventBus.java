package com.lonni.im.core.event;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件总线
 * @author: Lonni
 * @date: 2023/2/17 0017 16:31
 */
public class EventBus implements Serializable {

    private final  static ConcurrentHashMap<Integer,IEvent> event_maps=new ConcurrentHashMap<>();
    public static  EventBus getInstance(){
        return Singleton.get(EventBus.class);
    }

    public void  putEvent(Integer key,IEvent event ){
        event_maps.putIfAbsent(key,event);
    }


    public IEvent  getEvent(Integer type){
        Assert.notNull(type,"事件key值不能为空!");
        return event_maps.getOrDefault(type,null);
    }



}



