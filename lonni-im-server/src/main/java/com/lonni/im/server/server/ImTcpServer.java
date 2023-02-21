package com.lonni.im.server.server;

import cn.hutool.core.lang.Assert;
import com.lonni.im.server.init.ImTcpServerInitializer;
import com.lonni.im.server.properties.ImServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * netty服务端
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 18:09
 */
@Component
@ConditionalOnExpression(value = "#{ 'true'.equals(environment.getProperty('im.server.unionServer'))  || ('true').equals(environment.getProperty('im.server.enableTcp')) }")
@Slf4j
public class ImTcpServer implements Serializable {

    private EventLoopGroup boosGroup;
    private EventLoopGroup workGroup;
    private ServerBootstrap bootstrap;
    private ChannelFuture channelFuture;


    @Autowired
    private ImServerProperties serverProperties;

    @Autowired
    private ImTcpServerInitializer tcpServerInitializer;


    public SocketAddress getAddressByUnion() {
        String ip = serverProperties.getTcpIp();
        Integer port = serverProperties.getTcpPort();
        if (serverProperties.isUnionServer()) {
            Assert.notBlank(ip, "ip地址不能为空!");
            Assert.notNull(port, "端口不能为空!");
            return new InetSocketAddress(ip, port);
        }
        return null;
    }

    public SocketAddress getAddressByTcp() {
        String ip = serverProperties.getTcpIp();
        Integer port = serverProperties.getTcpPort();
        if (serverProperties.isEnableTcp()) {
            Assert.notBlank(ip, "ip地址不能为空!");
            Assert.notNull(port, "端口不能为空!");
            return new InetSocketAddress(ip, port);
        }
        return null;
    }




    /**
     * 启动服务
     */

//    @Async
    public void start() throws InterruptedException {
        boosGroup = new NioEventLoopGroup(serverProperties.getBoosGroupNum());
        workGroup = new NioEventLoopGroup(serverProperties.getWorkGroupNum());
        SocketAddress address = getAddressByUnion();
        address = address == null ? getAddressByTcp() : address;
        bootstrap=new ServerBootstrap();
        bootstrap.group(boosGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //将小的数据包包装成更大的帧进行传送，提高网络的负载,即TCP延迟传输
                .childOption(ChannelOption.TCP_NODELAY, false)
                //加入日志处理
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .localAddress(address)
                .childHandler(tcpServerInitializer)
        ;
        channelFuture = bootstrap.bind();
        //监听是否启动成功
        SocketAddress finalAddress = address;
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
             log.info("tcp 服务启动成功,监听端口为 :{}", finalAddress.toString());
             //TODO 注册服务
            }
        });
        /**
         * 添加钩子函数
         */
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                workGroup.shutdownGracefully();
                boosGroup.shutdownGracefully();
            }
        }));


        try {
            //  监听通道关闭事件
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
            workGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }


}



