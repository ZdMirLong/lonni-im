package com.lonni.im.client.boot;

import com.lonni.im.client.handler.ClientMessageHandler;
import com.lonni.im.client.properties.ImClientProperties;
import com.lonni.im.core.protocol.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.Serializable;
import java.net.URISyntaxException;

/**
 * TcpClient：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 11:16
 */
public class TcpClient extends AbstractClient {
    private ImClientProperties config;

    public TcpClient(ImClientProperties clientProperties) {
        super(clientProperties);
        this.config = clientProperties;
    }

    @Override
    public void initChannelInitializer(Bootstrap bootstrap) throws URISyntaxException {
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                pipeline.addLast("tcpCodec",MessageCodec.getInstance());
                pipeline.addLast("eventHandler", ClientMessageHandler.getInstance());
            }
        });
    }

    @Override
    public String getClientName() {
        return "tcp socket";
    }
}



