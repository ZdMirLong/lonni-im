package com.lonni.im.client.handler;

import cn.hutool.core.lang.Singleton;
import com.lonni.im.client.config.ImClientConfig;
import com.lonni.im.client.properties.ImClientProperties;
import com.lonni.im.core.annotation.MsgHandler;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.net.URI;

/**
 * WsClientHandler：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 10:49
 */

@Slf4j
public class WsClientHandler extends SimpleChannelInboundHandler<Object> {
    private  WebSocketClientHandshaker webSocketClientHandshaker;
    private ChannelPromise handshakeFuture;

    public WsClientHandler(WebSocketClientHandshaker handshaker) {
        webSocketClientHandshaker = handshaker;
    }



    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    /**
     * 异常
     *
     * @param channelHandlerContext channelHandlerContext
     * @param cause                 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        log.info("\n\t客户端发生错误⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" + "\t├ [exception]: {}\n" + "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", cause.getMessage());
        // 移出通道
        channelHandlerContext.close();
    }

    /**
     * 当客户端主动链接服务端的链接后，调用此方法
     *
     * @param channelHandlerContext ChannelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" + "\t├ [建立连接]\n" + "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓");
        Channel channel = channelHandlerContext.channel();
        //TODO  握手
        webSocketClientHandshaker.handshake(channel);

    }

    /**
     * 与服务端断开连接时
     *
     * @param ctx channelHandlerContext
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" + "\t├ [断开连接]：client [{}]\n" + "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", ctx.channel().remoteAddress());
        ctx.close();

    }

    /**
     * 读完之后调用的方法
     *
     * @param channelHandlerContext ChannelHandlerContext
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("channelReadComplete");
       channelHandlerContext.flush();
    }


    /**
     * 接收消息
     *
     * @param channelHandlerContext channelHandlerContext
     * @param msg                   msg
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        log.info("客户端接受到服务端消息....{}",msg.toString());
        Channel channel = channelHandlerContext.channel();
        //判断握手是否成功
        if (!webSocketClientHandshaker.isHandshakeComplete()) {
            log.info("握手成功");
            webSocketClientHandshaker.finishHandshake(channel, (FullHttpResponse) msg);
            handshakeFuture.setSuccess();
            //TODO 握手成功 保存链接等
            return;
        }


        // 只处理本协议的数据 其他数据直接报错
        if (msg instanceof BinaryWebSocketFrame){
            //调用retain
//           super.channelRead(channelHandlerContext,((BinaryWebSocketFrame) msg).retain());
            channelHandlerContext.fireChannelRead(((BinaryWebSocketFrame) msg).retain());
           return;
        }
        //其他协议的  清除出站
        channelHandlerContext.flush();
        // 关闭链接
        channelHandlerContext.close();
        log.info("检测到其他协议,关闭链接!");
    }
}



