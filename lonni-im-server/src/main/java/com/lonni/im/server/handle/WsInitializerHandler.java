package com.lonni.im.server.handle;

import io.netty.channel.ChannelPipeline;

/**
 * websocket协议增加管道处理器
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 10:08
 */
public interface WsInitializerHandler {

    /**
     * 初始化管道
     * @param pipeline
     */
    public  void  initPieLine(ChannelPipeline pipeline);


}
