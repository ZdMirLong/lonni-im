package com.lonni.im.client.boot;

import com.lonni.im.client.handler.ClientMessageHandler;
import com.lonni.im.client.handler.WsClientHandler;
import com.lonni.im.client.properties.ImClientProperties;
import com.lonni.im.core.protocol.WsMessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * WsClient：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 10:25
 */
public class WsClient extends AbstractClient {

    private final Logger logger = LoggerFactory.getLogger(WsClient.class);

    private ImClientProperties config;


    WebSocketClientHandshaker webSocketClientHandshaker;

    WsClientHandler wsClientHandler;




    public WsClient(ImClientProperties properties) throws URISyntaxException {
        super(properties);
        this.config = properties;
        webSocketClientHandshaker=  WebSocketClientHandshakerFactory.newHandshaker(
                new URI("ws://"+config.getIp()+":"+config.getPort()+config.getWsPrefix())
                , WebSocketVersion.V13
                , null
                , false
                , new DefaultHttpHeaders()
        );
        wsClientHandler=new WsClientHandler(webSocketClientHandshaker);

    }

    /**
     * 初始化
     * @param bootstrap
     */
    @Override
    public void initChannelInitializer(Bootstrap bootstrap) throws URISyntaxException {
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                //加入日志处理
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                // 将请求与应答消息编码或者解码为HTTP消息
                pipeline.addLast(new HttpClientCodec());
                // 将http消息的多个部分组合成一条完整的HTTP消息
                pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                // 客户端Handler 用于握手和判断类型
                pipeline.addLast("handler", wsClientHandler);
                // 加入解码器
                pipeline.addLast("wsCodec", WsMessageCodec.getInstance());
                // 添加协议分发处理器
                pipeline.addLast("eventHandler", ClientMessageHandler.getInstance());
            }
        });

    }

    @Override
    public String getClientName() {
        return "websocket";
    }
}



