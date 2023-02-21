package com.lonni.im.server.handle.dispatcher;

import com.lonni.im.core.util.ImUtil;
import com.lonni.im.server.handle.TcpInitializerHandler;
import com.lonni.im.server.handle.WsInitializerHandler;
import com.lonni.im.server.properties.ImServerProperties;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * <h1>通道预处理器</h1>
 * 用于切换tcp 或ws协议
 *  不能设置共享
 * @author: Lonni
 * @date: 2023/2/17 0017 9:13
 */
@Component
@Slf4j
public class ProtocolDispatcher extends ByteToMessageDecoder {
    private TcpInitializerHandler tcpInitializerHandler;
    private WsInitializerHandler wsInitializerHandler;

    private ImServerProperties config;

    @Autowired
    public ProtocolDispatcher(ImServerProperties properties,
                              TcpInitializerHandler tcpInitializerHandler,
                              WsInitializerHandler wsInitializerHandler
    ) {
        this.config = properties;
        this.tcpInitializerHandler = tcpInitializerHandler;
        this.wsInitializerHandler = wsInitializerHandler;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("ProtocolDispatcher--->decode");
        if (in.readableBytes() < 5) {
            return;
        }
        //读取到第一二个字节 用于判断是否是http协议
        int readerIndex = in.readerIndex();
        final int magic1 = in.getByte(readerIndex);
        final int magic2 = in.getByte(readerIndex + 1);
        // 读取完之后重置读索引
        in.resetReaderIndex();
        //读取4个字节,判断是否是tcp自定义协议
        byte[] moshuBy = new byte[4];
        in.readBytes(moshuBy);
        // 取完之后重置读索引 交给下一个处理链
        in.resetReaderIndex();
//        com.lonni.im.core.protocol.MessageCodec.getMagics()
        //判断是否是tcp
        boolean istcp = ImUtil.getInstance().checkMagics(moshuBy);
        if (istcp) {
            log.info("ProtocolDispatcher--->是tcp协议");
            dispatchToTcp(ctx,in);
        } else if (ImUtil.getInstance().isHttp(magic1, magic2)) {
            //判断是否是http
            log.info("ProtocolDispatcher--->是http协议");
            dispatchToHttp(ctx);
        } else {
            in.clear();
            ctx.close();
        }


    }

    public void dispatchToTcp(ChannelHandlerContext ctx,Object msg) {
        ChannelPipeline pipeline = ctx.pipeline();
        //加入自定义编解码
        tcpInitializerHandler.initPieLine(pipeline);
        if (config.isUnionServer()) {
            pipeline.remove(this);
            ctx.fireChannelActive();
        }
    }

    public void dispatchToHttp(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        wsInitializerHandler.initPieLine(pipeline);
        if (config.isUnionServer()) {
            pipeline.remove(this);
        }
        // 将channelActive事件传递到PacketHandler
        ctx.fireChannelActive();

    }

}



