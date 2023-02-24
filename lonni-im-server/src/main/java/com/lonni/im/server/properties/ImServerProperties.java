package com.lonni.im.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * ImServerProperties：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 18:00
 */
@ConfigurationProperties("im.server")
@Data

public class ImServerProperties implements Serializable {


    /**
     * boos线程主的数量
     */
    private Integer boosGroupNum = 1;

    /**
     * work线程组的数量
     */
    private Integer workGroupNum = 5;
    /**
     * 是否使用联合server
     * 即 将websocket和tcp放在同一端口
     */
    private boolean unionServer = false;
    /**
     * 是否启用tcp服务
     * 如果unionServer=true,此配置无效
     */
    private boolean enableTcp;
    /**
     * 是否启用 websocket
     * 如果unionServer=true,此配置无效
     */
    private boolean enableWs;

    /**
     * tcp ip地址
     * 如果unionServer=true,此为ws 和 tcp的ip地址
     */
    private String tcpIp;
    /**
     * tcp 端口
     * 如果unionServer=true,此为ws 和 tcp的端口
     */
    private Integer tcpPort;

    /**
     * ws的ip地址
     * 如果unionServer=false,此为ws的ip
     */
    private String wsIp;

    /**
     * ws的端口
     * 如果unionServer=false,此为ws的ip
     */
    private Integer wsPort;

    /**
     * ws的前缀
     * 默认为 /ws
     */
    private String wsPrefix = "/ws";

    /**
     * 心跳超时时间 单位  毫秒 ,默认 20000
     */
    private Long heartBeatTime=20000L;


}



