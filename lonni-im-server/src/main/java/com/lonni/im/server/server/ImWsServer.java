package com.lonni.im.server.server;

import cn.hutool.core.lang.Assert;
import com.lonni.im.server.init.ImTcpServerInitializer;
import com.lonni.im.server.init.ImWsServerInitializer;
import com.lonni.im.server.properties.ImServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * netty websocket 服务端
 * @author: Lonni
 * @date: 2023/2/16 0016 18:09
 */
public class ImWsServer extends AbstractServer {
    private final Logger logger = LoggerFactory.getLogger(ImWsServer.class);

    private ImServerProperties serverProperties;
    private ImWsServerInitializer wsServerInitializer;
    public ImWsServer(ImServerProperties serverProperties,  ImWsServerInitializer wsServerInitializer) {
        super(serverProperties);
        this.serverProperties=serverProperties;
        this.wsServerInitializer=wsServerInitializer;
    }

    /**
     * 绑定客户端
     */
    @Override
    public void initChannelInitializer(ServerBootstrap bootstrap) {
        bootstrap.childHandler(wsServerInitializer);
    }

    @Override
    public String getServerName() {
        return  "web socket";
    }
    @Override
    public SocketAddress getAddress() {
        String ip = serverProperties.getWsIp();
        Integer port = serverProperties.getWsPort();
        if (serverProperties.isEnableWs()) {
            Assert.notBlank(ip, "ip地址不能为空!");
            Assert.notNull(port, "端口不能为空!");
            return new InetSocketAddress(ip, port);
        }
        return null;
    }


}



