package com.lonni.im.core.protocol;

import cn.hutool.core.lang.Singleton;
import com.lonni.im.core.action.Action;
import com.lonni.im.core.util.SpringContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 用于websocket编解码器
 * 实现和 MessageCodec一样 返回二进制数据
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 10:34
 */
@ChannelHandler.Sharable
@Slf4j
public class WsMessageCodec extends MessageToMessageCodec<WebSocketFrame, Action> {

    public static WsMessageCodec getInstance() {
        return Singleton.get(WsMessageCodec.class);
    }

    /**
     * 编解码实现类
     */
    public CustomCodec codec = SpringContext.getBean(CustomCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Action msg, List<Object> out) throws Exception {
        ByteBuf in = ByteBufAllocator.DEFAULT.buffer();
        codec.encode(msg, in);
        out.add(new BinaryWebSocketFrame(in));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf in = msg.content();
        codec.decode(ctx, in, out);
    }
}



