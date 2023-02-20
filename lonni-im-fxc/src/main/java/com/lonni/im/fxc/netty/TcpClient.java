package com.lonni.im.fxc.netty;

import com.lonni.im.core.action.LoginRequestAction;
import com.lonni.im.core.protocol.MessageCodec;
import com.lonni.im.core.serializable.SerializeAlgorithmEnum;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * TcpClient：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 12:08
 */
@Component
public class TcpClient implements Serializable {


    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;


    private     ChannelFuture channelFuture;

    public void  sendMessage(){
        System.out.println("发送消息");
        LoginRequestAction loginAction = new LoginRequestAction();
        loginAction.setSeriaType(SerializeAlgorithmEnum.JDK.getTypeToInt());
        loginAction.setPwd("111");
        loginAction.setUserName("222");
        channelFuture.channel().writeAndFlush(loginAction);
        System.out.println("发送消息完成");
    }



    @Async
    public void start() throws InterruptedException {
        String ip="127.0.0.1";
        Integer port=9898;


        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class)
                //4 设置通道的选项参数， 对于服务端而言就是ServerSocketChannel， 客户端而言就是SocketChannel；
                .option(ChannelOption.SO_KEEPALIVE, true)
//                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        //加入日志处理
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        //加入编解码期
                        pipeline.addLast(new  MessageCodec());
                        pipeline.addLast(new ClientHandler());
                    }
                });

         channelFuture = bootstrap.connect(new InetSocketAddress(ip, port)).sync();
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



