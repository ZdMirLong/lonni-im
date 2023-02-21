package com.lonni.im.server.init;

import com.lonni.im.server.handle.dispatcher.ProtocolDispatcher;
import com.lonni.im.server.handle.TcpInitializerHandler;
import com.lonni.im.server.handle.WsInitializerHandler;
import com.lonni.im.server.properties.ImServerProperties;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 协议初始化器
 * @author: Lonni
 * @date: 2023/2/16 0016 18:16
 */
@Component
public class ImTcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private ImServerProperties config;
    private TcpInitializerHandler tcpInitializerHandler;
    private WsInitializerHandler wsInitializerHandler;



    @Autowired
    public ImTcpServerInitializer(ImServerProperties properties,
                                  TcpInitializerHandler tcpInitializerHandler,
                                  WsInitializerHandler wsInitializerHandler
                                  ){
        this.config=properties;
        this.tcpInitializerHandler=tcpInitializerHandler;
        this.wsInitializerHandler=wsInitializerHandler;
    }

    /**
     * 初始化通道
     * 在这里增加tcp ws 处理通道
     * @param ch            the {@link Channel} which was registered.
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (config.isUnionServer()){
            //如果是联合端口 ,使用此方式
            pipeline.addLast(new ProtocolDispatcher(config,tcpInitializerHandler,wsInitializerHandler));
        }else {
            //否则直接使用此方式
           tcpInitializerHandler.initPieLine(pipeline);
        }

    }
}



