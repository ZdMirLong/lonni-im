package com.lonni.im.core.util;

import cn.hutool.core.lang.ClassScanner;
import cn.hutool.core.lang.Singleton;
import com.lonni.im.core.annotation.MsgHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final static Logger logger = LoggerFactory.getLogger(MsgHandlerUtil.class);

    /**
     * 获取处理类,返回排序后的集合
     *
     * @return
     */
    public static List<Object> getMsgHandler() {
        List<Object> objects = new ArrayList<>();
        List<Map<Object, Object>> maps = new ArrayList<>();
        Map<String, Object> beansWithAnnotation = SpringContext.getBeansWithAnnotation(MsgHandler.class);
        beansWithAnnotation.forEach((k, v) -> {
            MsgHandler annotation = v.getClass().getAnnotation(MsgHandler.class);
            int order = annotation.order();
            maps.add(new ConcurrentHashMap<Object, Object>() {{
                put("order", order);
                put("bean", v);
            }});
        });
        maps.sort(Comparator.comparing(
                (Map<Object, Object> h) -> Integer.parseInt(h.get("order").toString()))
        );
        maps.forEach(p -> {
            objects.add(p.get("bean"));
        });
        return objects;


    }

    /**
     * 将自定义的消息处理器添加到
     *
     * @param pipeline
     */
    public static void addMsgHandlerToPipeline(ChannelPipeline pipeline) {
        List<Object> msgHandler = getMsgHandler();
        logger.info("获取自定义消息处理器,数量为:{}",msgHandler.size());
        for (Object bean : msgHandler) {
            pipeline.addLast((ChannelHandler) Singleton.get(bean.getClass()));
        }

    }


}



