package com.lonni.im.server.handle;

import com.lonni.im.core.protocol.WsMessageCodec;
import com.lonni.im.server.model.ImServerProperties;
import com.lonni.im.server.msghandler.LoginHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 用于处理websocket协议器
 * 因为websocket有多种消息协议 ,所以需要设置为object,否则无法进入
 * 在这里增加编解码器
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 10:22
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<Object> {


    @Autowired
    private ImServerProperties properties;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof PingWebSocketFrame) {
            pingWebSocketFrameHandler(ctx, (PingWebSocketFrame) msg);
        } else if (msg instanceof TextWebSocketFrame) {
            textWebSocketFrameHandler(ctx, (TextWebSocketFrame) msg);
        } else if (msg instanceof CloseWebSocketFrame) {
            closeWebSocketFrameHandler(ctx, (CloseWebSocketFrame) msg);
        } else if (msg instanceof BinaryWebSocketFrame) {
            log.info("websocket --->channelRead0,是BinaryWebSocketFrame类型");
            //支持二进制消息
            this.addHandler(ctx);
            //触发下一个处理器的ChannelRead方法
            ctx.fireChannelRead(((BinaryWebSocketFrame) msg).retain());
        } else if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            //如果是http请求
            if (handToWebSocket(ctx, request)) {
                ctx.fireChannelRead(((FullHttpRequest) msg).retain());
            } else {
                //TODO 如果是http请求
                log.info("不支持的请求 http");
                ctx.close();
            }
        }
    }


    private boolean handToWebSocket(ChannelHandlerContext ctx, FullHttpRequest request) {
        HttpHeaders headers = request.headers();
        if ("Upgrade".equalsIgnoreCase(headers.get(HttpHeaderNames.CONNECTION)) &&
                "WebSocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {
            ChannelPipeline pipeline = ctx.pipeline();
            log.info("websocket --->httphandler->handToWebSocket ");
            this.addHandler(ctx);
            return true;
        }
        return false;
    }

    public void addHandler(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        //TODO 其他处理逻辑
        pipeline.addLast(WsMessageCodec.getInstance());
        pipeline.addLast(new LoginHandler());
        if (properties.isUnionServer()) {
            pipeline.remove(this);
            // 将channelActive事件传递到FrameHandler
            ctx.fireChannelActive();
        }



    }


    /**
     * 客户端发送断开请求处理
     *
     * @param ctx
     * @param frame
     */
    private void closeWebSocketFrameHandler(ChannelHandlerContext ctx, CloseWebSocketFrame frame) {
        log.debug("接收到主动断开请求：{}", ctx.channel().id());
        ctx.close();
    }

    /**
     * 创建连接之后，客户端发送的消息都会在这里处理
     *
     * @param ctx
     * @param frame
     */
    private void textWebSocketFrameHandler(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String text = frame.text();
        log.debug("textWebSocketFrameHandler接收到客户端的消息：{}", text);
        // 将客户端消息回送给客户端
//        webSocketChannelPool.writeAndFlush(new TextWebSocketFrame("接收到客户端的消息：" + text));

    }

    /**
     * 处理客户端心跳包
     *
     * @param ctx
     * @param frame
     */
    private void pingWebSocketFrameHandler(ChannelHandlerContext ctx, PingWebSocketFrame frame) {
        ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
    }

}



