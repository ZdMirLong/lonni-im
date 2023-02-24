package com.lonni.im.server.handle.impl;

import com.lonni.im.core.protocol.MessageCodec;
import com.lonni.im.core.util.MsgHandlerUtil;
import com.lonni.im.server.handle.HeartBeatHandler;
import com.lonni.im.server.handle.MessageHandler;
import com.lonni.im.server.handle.TcpInitializerHandler;
import com.lonni.im.server.properties.ImServerProperties;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TcpDefaultInitializerHandler：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 10:11
 */
@Component
public class TcpDefaultInitializerHandler implements TcpInitializerHandler {

    @Autowired
    private ImServerProperties serverProperties;

    @Override
    public void initPieLine(ChannelPipeline pipeline) {
        pipeline.addLast(new MessageCodec());
        pipeline.addLast(new IdleStateHandler(
                0, 0,
                10));
        pipeline.addLast(new HeartBeatHandler(serverProperties.getHeartBeatTime()));
        //TODO 去掉 自定义的handler 去掉 ,使用事件分发处理
//        MsgHandlerUtil.addMsgHandlerToPipeline(pipeline);
        pipeline.addLast(MessageHandler.getInstance());



    }
}



