package com.lonni.im.client;

import com.lonni.im.core.protocol.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * testTcpClient：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 11:32
 */
public class testTcpClient implements Serializable {


    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;





    @Test
    public void start() throws InterruptedException {
        String ip="127.0.0.1"; Integer port=9898;


        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class)
                //4 设置通道的选项参数， 对于服务端而言就是ServerSocketChannel， 客户端而言就是SocketChannel；
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        //加入日志处理
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        //加入编解码期
                        pipeline.addLast(MessageCodec.getInstance());
                        pipeline.addLast(new ClientHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(ip, port)).sync();
        // 增加钩子函数
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                workerGroup.shutdownGracefully();
            }
        }));


        try {
            // 7 监听通道关闭事件
            // 应用程序会一直等待，直到channel关闭
            ChannelFuture closeFuture =
                    channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (
                Exception e) {
            System.out.println("发生其他异常");
            e.printStackTrace();

        } finally {
            // 8 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerGroup.shutdownGracefully();

        }


    }







}



