package com.lonni.im.server.server;

import com.lonni.im.core.util.Osutil;
import com.lonni.im.server.properties.ImServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * 服务端的抽象类
 *
 * @author: Lonni
 * @date: 2023/2/23 0023 15:50
 */
public abstract class AbstractServer implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    String logBanner = "\n\n" +
            "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
            "*                                                                                   *\n" +
            "*                                                                                   *\n" +
            "*                   {} Server started on  {}.                        *\n" +
            "*                                                                                   *\n" +
            "*                                                                                   *\n" +
            "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n";

    private EventLoopGroup boosGroup;
    private EventLoopGroup workGroup;

    private ImServerProperties config;

    public AbstractServer(ImServerProperties properties) {
        this.config = properties;
        ThreadFactory boosFactory = r -> {
            Thread thread = new Thread(r);
            thread.setName("nio-boos-" + thread.getId());
            return thread;
        };
        ThreadFactory workFactory = r -> {
            Thread thread = new Thread(r);
            thread.setName("nio-work-" + thread.getId());
            return thread;
        };
        if (Osutil.isLinux()) {
            boosGroup = new EpollEventLoopGroup(config.getBoosGroupNum(), boosFactory);
            workGroup = new EpollEventLoopGroup(config.getWorkGroupNum(), workFactory);
        } else {
            boosGroup = new NioEventLoopGroup(config.getBoosGroupNum(), boosFactory);
            workGroup = new NioEventLoopGroup(config.getWorkGroupNum(), workFactory);
        }

    }


    public abstract SocketAddress getAddress();

    public abstract void initChannelInitializer(ServerBootstrap bootstrap);

    public abstract String getServerName();

    public void start() {
        ServerBootstrap bootstrap = initBootstrap();
        this.initChannelInitializer(bootstrap);
        ChannelFuture channelFuture = bootstrap.bind().syncUninterruptibly();
        bindAfter(this.getServerName(), channelFuture);

    }


    protected ServerBootstrap initBootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boosGroup, workGroup);
        bootstrap.channel(Osutil.isLinux() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //将小的数据包包装成更大的帧进行传送，提高网络的负载,即TCP延迟传输
                .childOption(ChannelOption.TCP_NODELAY, false)
                //加入日志处理
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .localAddress(getAddress());
        return bootstrap;
    }

    void bindAfter(String serverName, ChannelFuture channelFuture) {
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            /**
             * 注册jvm钩子函数
             */
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    logger.info("jvm钩子函数,优雅关闭netty...");
                    workGroup.shutdownGracefully();
                    boosGroup.shutdownGracefully();
                }
            }));
            logger.info(logBanner, serverName, ((InetSocketAddress) getAddress()).toString());
        });
        channelFuture.channel().closeFuture().addListener(future -> this.destroy());

    }

    public void destroy() {
        if (boosGroup != null && !boosGroup.isShuttingDown() && !boosGroup.isShutdown()) {
            try {
                boosGroup.shutdownGracefully();
            } catch (Exception ignore) {
            }
        }

        if (workGroup != null && !workGroup.isShuttingDown() && !workGroup.isShutdown()) {
            try {
                workGroup.shutdownGracefully();
            } catch (Exception ignore) {
            }
        }
    }

}



