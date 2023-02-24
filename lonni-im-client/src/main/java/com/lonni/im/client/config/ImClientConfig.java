package com.lonni.im.client.config;

import com.lonni.im.client.boot.TcpClient;
import com.lonni.im.client.boot.WsClient;
import com.lonni.im.client.properties.ImClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.net.URISyntaxException;

/**
 * ImClientConfig：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 9:45
 */
@Configuration
@EnableConfigurationProperties(value = ImClientProperties.class)
public class ImClientConfig implements Serializable
{

    @Bean(name = "wsClient",destroyMethod = "destroy")
    @ConditionalOnProperty(value = "im.client.linkType",havingValue = "1")
    public WsClient wsClient(ImClientProperties properties) throws URISyntaxException {
        return new WsClient(properties);
    }
    @Bean(name = "tcpClient",destroyMethod = "destroy")
    @ConditionalOnProperty(value = "im.client.linkType",havingValue = "0")
    public TcpClient tcpClient(ImClientProperties properties) throws URISyntaxException {
        return new TcpClient(properties);
    }

}



