package com.lonni.im.core.protocol;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import com.lonni.im.core.action.Action;
import com.lonni.im.core.action.ActionFactory;
import com.lonni.im.core.constains.GloadConstains;
import com.lonni.im.core.handler.MagicsHandler;
import com.lonni.im.core.serializable.SerializeFactory;
import com.lonni.im.core.serializable.SerializerBase;
import com.lonni.im.core.util.SpringContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * <p>消息编解码器</p>
 * <br/>
 * <p>自定义协议规定:</p>
 * 1:魔数  4个字节 暂定为 1234 <br/>
 * 2:版本  1个字节 默认为1 <br/>
 * 3:序列化算法,如 jdk:0  json:1  hessian:2  protobuf:3 kryo:4   1字节 <br/>
 * 4:消息类型,是登录 注册  或业务相关的  1字节 <br/>
 * 5:请求序号,为了双工通信,提供异步能力  4字节   (int占4个字节) <br/>
 * 6: 一字节  无意义,为了满足2的整数倍,所以增加一个字节  1字节<br/>
 * 7:正文长度  4字节<br/>
 * 8:消息体
 *
 * @author: Lonni
 * @date: 2023/2/6 0006 14:50
 */
public class MessageCodec extends ByteToMessageCodec<Action> {
    private final Logger log = LoggerFactory.getLogger(MessageCodec.class);

    public static MessageCodec getInstance() {
        return Singleton.get(MessageCodec.class);
    }

    /**
     * 编解码实现类
     */
    public CustomCodec codec = SpringContext.getBean(CustomCodec.class);


    /**
     * 编码
     *
     * @param channelHandlerContext
     * @param baseMessage
     * @param out
     * @throws Exception
     */
    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, Action baseMessage, ByteBuf out) throws Exception {
        codec.encode(baseMessage,out);
    }

    /**
     * 解码
     *
     * @param ctx
     * @param in
     * @param list
     * @throws Exception
     */
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        codec.decode(ctx,in,list);
    }
}



