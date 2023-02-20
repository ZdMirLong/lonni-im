package com.lonni.im.server.handle.impl;

import com.lonni.im.server.handle.HttpHandler;
import com.lonni.im.server.handle.RelayHandler;
import com.lonni.im.server.handle.TcpInitializerHandler;
import com.lonni.im.server.handle.WsInitializerHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
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
public class WsDefaultInitializerHandler implements WsInitializerHandler {
    private HttpHandler httpHandler;

    @Autowired
    public WsDefaultInitializerHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }


    @Override
    public void initPieLine(ChannelPipeline pipeline) {
        //websock是基于http的,需要加入http编解码器
        pipeline.addLast(new HttpServerCodec());
        //添加对于读写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
        // ================= 上述是用于支持http协议的 ==============
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 添加中继处理
        pipeline.addLast(new RelayHandler());
        //加入自定义的处理器
        pipeline.addLast(httpHandler);

    }
}



