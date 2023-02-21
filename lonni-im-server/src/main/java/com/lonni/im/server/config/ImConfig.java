package com.lonni.im.server.config;

import com.lonni.im.server.properties.ImServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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


}



