package com.lonni.im.server.init;

import com.lonni.im.server.handle.WsInitializerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 协议初始化器
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 18:16
 */
@Component
public class ImWsServerInitializer extends ChannelInitializer<SocketChannel> {

    private WsInitializerHandler wsInitializerHandler;

    @Autowired
    public ImWsServerInitializer(WsInitializerHandler wsInitializerHandler) {
        this.wsInitializerHandler = wsInitializerHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        wsInitializerHandler.initPieLine(pipeline);
    }
}



