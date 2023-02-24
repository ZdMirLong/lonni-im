package com.lonni.im.client.handler;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ReUtil;
import com.lonni.im.core.action.Action;
import com.lonni.im.core.event.EventBus;
import com.lonni.im.core.event.IEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * ClientMessageHandler：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 11:09
 */
@ChannelHandler.Sharable
@Slf4j
public class ClientMessageHandler extends SimpleChannelInboundHandler<Action> {

    public  static  ClientMessageHandler getInstance(){
         return Singleton.get(ClientMessageHandler.class);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("链接完成...");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Action msg) throws Exception {
        log.info("接受到数据:{}",msg.toString());
        IEvent event = EventBus.getInstance().getEvent(msg.getMessageType());
        if (event==null){
            log.error("找不到消息处理事件....");
            return;
        }
        Object execute = event.execute(msg, ctx);
        if (execute!=null){
            ctx.writeAndFlush(execute);
        }
    }
}



