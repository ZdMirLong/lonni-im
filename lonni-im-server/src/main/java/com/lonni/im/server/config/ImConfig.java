package com.lonni.im.server.config;

import com.lonni.im.server.handle.TcpInitializerHandler;
import com.lonni.im.server.init.ImTcpServerInitializer;
import com.lonni.im.server.init.ImWsServerInitializer;
import com.lonni.im.server.properties.ImServerProperties;
import com.lonni.im.server.server.ImTcpServer;
import com.lonni.im.server.server.ImWsServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * ImConfig：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/21 0021 10:46
 */
@Configuration
@EnableConfigurationProperties(ImServerProperties.class)
public class ImConfig implements Serializable {



    @Bean(name = "imTcpServer",destroyMethod = "destroy")
    @ConditionalOnExpression(value = "#{ 'true'.equals(environment.getProperty('im.server.unionServer'))  || ('true').equals(environment.getProperty('im.server.enableTcp')) }")
    public ImTcpServer imTcpServer(ImServerProperties properties, ImTcpServerInitializer serverInitializer) {
        return  new ImTcpServer(properties,serverInitializer);
    }

    @Bean(name = "imWsServer",destroyMethod = "destroy")
    @ConditionalOnExpression(value = "#{  ('true').equals(environment.getProperty('im.server.enableWs')) }")
    public ImWsServer imWsServer(ImServerProperties properties, ImWsServerInitializer serverInitializer) {
        return  new ImWsServer(properties,serverInitializer);
    }


}



