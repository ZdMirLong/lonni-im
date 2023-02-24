package com.lonni.im.server.handle;

import com.lonni.im.core.constains.GloadConstains;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 空闲检测处理器
 * 从通道中拿到上一次读写的时间和当前时间比较,如果时间差大于传入的时间,视为超时,关闭链接
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 15:43
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    private Long heartBeatTime;

    public HeartBeatHandler(Long heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info(" server  读空闲");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info(" server  写空闲");
            } else {
                //全空闲
                long nowTime = System.currentTimeMillis();
                Long lastReadTime = (Long) ctx.channel().attr(AttributeKey.valueOf(GloadConstains.ReadTime)).get();
                if (lastReadTime != null && nowTime - lastReadTime > heartBeatTime) {
                    //TODO 退后台逻辑 移除session 关闭链接
                    ctx.close();
                }


            }


        }
    }
}



