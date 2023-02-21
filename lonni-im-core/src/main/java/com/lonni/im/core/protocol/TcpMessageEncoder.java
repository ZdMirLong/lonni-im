package com.lonni.im.core.protocol;

import com.lonni.im.core.action.Action;
import com.lonni.im.core.util.SpringContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * tcp方式编码器
 * @author: Lonni
 * @date: 2023/2/20 0020 17:00
 */
@ChannelHandler.Sharable
@Deprecated
public class TcpMessageEncoder extends MessageToByteEncoder<Action> {
    private final Logger log = LoggerFactory.getLogger(TcpMessageEncoder.class);

    /**
     * 编解码实现类
     */
    public CustomCodec codec = SpringContext.getBean(CustomCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Action msg, ByteBuf out) throws Exception {
        codec.encode(msg,out);
    }
}



