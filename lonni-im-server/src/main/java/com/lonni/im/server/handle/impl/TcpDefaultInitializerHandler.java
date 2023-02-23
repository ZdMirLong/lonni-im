package com.lonni.im.server.handle.impl;

import com.lonni.im.core.protocol.MessageCodec;
import com.lonni.im.core.util.MsgHandlerUtil;
import com.lonni.im.server.handle.MessageHandler;
import com.lonni.im.server.handle.TcpInitializerHandler;
import io.netty.channel.ChannelPipeline;
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

    @Override
    public void initPieLine(ChannelPipeline pipeline) {
        pipeline.addLast(new MessageCodec());
        //TODO 去掉 自定义的handler 去掉 ,使用事件分发处理
//        MsgHandlerUtil.addMsgHandlerToPipeline(pipeline);
        pipeline.addLast(MessageHandler.getInstance());



    }
}



