package com.lonni.im.server.handle.impl;

import com.lonni.im.core.protocol.MessageCodec;
import com.lonni.im.server.handle.TcpInitializerHandler;
import com.lonni.im.server.model.ImServerProperties;
import com.lonni.im.server.msghandler.LoginHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * TcpDefaultInitializerHandler：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 10:11
 */
@Component
public class TcpDefaultInitializerHandler implements TcpInitializerHandler {

    @Override
    public void initPieLine(ChannelPipeline pipeline) {
        pipeline.addLast(MessageCodec.getInstance());
        pipeline.addLast(new LoginHandler());

    }
}



