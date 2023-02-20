package com.lonni.im.core.event;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * 事件操作对象
 * @author: Lonni
 * @date: 2023/2/17 0017 16:29
 */
public interface IEvent<T,R>  {

    /**
     * 事件对象
     * @param requestAction
     * @param ctx
     * @return
     */
    R execute(T requestAction, ChannelHandlerContext ctx);

}



