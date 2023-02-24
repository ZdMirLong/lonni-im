package com.lonni.im.server.handle;

import cn.hutool.core.lang.Singleton;
import com.lonni.im.core.action.Action;
import com.lonni.im.core.event.EventBus;
import com.lonni.im.core.event.IEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 接受消息处理器
 * @author: Lonni
 * @date: 2023/2/23 0023 17:44
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<Action> {

    public static MessageHandler getInstance() {
        return Singleton.get(MessageHandler.class);
    }



    /**
     * 请求进入
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive....");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive....断开链接");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete....去读数据完成");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("userEventTriggered....触发事件");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught....发生异常", cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Action msg) throws Exception {
        log.info("进入读取事件....");
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



