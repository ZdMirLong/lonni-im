package com.lonni.im.server.handle.dispatcher;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.Serializable;

/**
 * 中继处理器
 * 用于统一执行下一个处理器的 ChannelRead 方法
 * 避免websocket和tcp协议不同一
 * @author: Lonni
 * @date: 2023/2/17 0017 15:23
 */
@ChannelHandler.Sharable
public class RelayHandler  extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("RelayHandler-----channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("RelayHandler-----channelRead");
        super.channelRead(ctx,msg);
    }

}



