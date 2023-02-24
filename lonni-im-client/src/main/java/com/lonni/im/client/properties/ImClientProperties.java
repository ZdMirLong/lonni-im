package com.lonni.im.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * ImClientProperties：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 9:36
 */
@Data
@ConfigurationProperties(value = "im.client")
public class ImClientProperties implements Serializable {

    /**
     * ip地址
     */
    private String ip;
    private Integer port;
    /**
     * 链接类型  0 tcp 1 ws ,如果是其他不开启
     */
    private Integer  linkType;
    /**
     * 如果是ws, 链接前缀是什么
     */
    private String wsPrefix;

    /**
     * 线程数
     */
    private Integer groupNum=0;

}



