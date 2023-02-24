package com.lonni.im.client.boot;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.lonni.im.client.properties.ImClientProperties;
import com.lonni.im.client.util.ImClient;
import com.lonni.im.core.util.Osutil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;

/**
 * 客户端链接基类
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 9:35
 */

public abstract class AbstractClient implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(AbstractClient.class);


    String logBanner = "\n\n" +
            "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
            "*                                                                                   *\n" +
            "*                                                                                   *\n" +
            "*                   {} client started on  {}.                        *\n" +
            "*                                                                                   *\n" +
            "*                                                                                   *\n" +
            "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n";


    private ImClientProperties config;
    private EventLoopGroup workGroup;
    private ChannelFuture channelFuture;

    public AbstractClient(ImClientProperties properties) {
        Assert.notBlank(properties.getIp(), "ip不能为空!");
        Assert.notNull(properties.getPort(), "端口不能为空!");
        if (properties.getLinkType() == 1) {
            properties.setWsPrefix(StrUtil.isBlank(properties.getWsPrefix()) ? "/ws" : properties.getWsPrefix());
        }
        this.config = properties;
        workGroup = Osutil.isLinux() ? new EpollEventLoopGroup(properties.getGroupNum()) : new NioEventLoopGroup(properties.getGroupNum());

    }

    public abstract void initChannelInitializer(Bootstrap bootstrap) throws URISyntaxException;

    public abstract String getClientName();

    public Bootstrap createBootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(Osutil.isLinux()? EpollSocketChannel.class:NioSocketChannel.class);
        return bootstrap;
    }

    @SneakyThrows
    public void start() {
        Bootstrap bootstrap = this.createBootstrap();
        this.initChannelInitializer(bootstrap);
        channelFuture = bootstrap.connect(new InetSocketAddress(config.getIp(), config.getPort())).syncUninterruptibly();
        this.bindAfter(this.getClientName(), channelFuture);


    }

    void bindAfter(String serverName, ChannelFuture channelFuture) {
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            ImClient.getInstance().setChannelFuture(channelFuture);
            /**
             * 注册jvm钩子函数
             */
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    logger.info("jvm钩子函数,优雅关闭netty...");
                    workGroup.shutdownGracefully();
                }
            }));
            logger.info(logBanner, serverName, channelFuture.channel().remoteAddress().toString());
        });
        channelFuture.channel().closeFuture().addListener(future -> this.destroy());

    }

    public void destroy() {
        if (workGroup != null && !workGroup.isShuttingDown() && !workGroup.isShutdown()) {
            try {
                workGroup.shutdownGracefully();
            } catch (Exception ignore) {
            }
        }
    }


}



