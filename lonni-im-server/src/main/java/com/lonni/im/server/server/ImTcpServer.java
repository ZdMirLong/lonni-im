package com.lonni.im.server.server;

import cn.hutool.core.lang.Assert;
import com.lonni.im.core.util.Osutil;
import com.lonni.im.server.init.ImTcpServerInitializer;
import com.lonni.im.server.properties.ImServerProperties;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * netty tcp 服务端
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 18:09
 */
public class ImTcpServer extends AbstractServer {
    private final Logger logger = LoggerFactory.getLogger(ImTcpServer.class);

    private ImServerProperties serverProperties;
    private ImTcpServerInitializer tcpServerInitializer;
    public ImTcpServer(ImServerProperties serverProperties,ImTcpServerInitializer tcpServerInitializer) {
        super(serverProperties);
        this.serverProperties=serverProperties;
        this.tcpServerInitializer=tcpServerInitializer;
    }

    /**
     * 绑定客户端
     */
    @Override
    public void initChannelInitializer( ServerBootstrap bootstrap) {
        bootstrap.childHandler(tcpServerInitializer);

    }

    @Override
    public String getServerName() {
        return  "tcp socket";
    }


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


    @Override
    public SocketAddress getAddress() {
        SocketAddress address = getAddressByUnion();
        address = address == null ? getAddressByTcp() : address;
        return address;
    }


}



