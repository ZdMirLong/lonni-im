package com.lonni.im.server.server;

import com.lonni.im.server.properties.ImServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * ImServerBoot：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 9:55
 */
@Component
@Slf4j
public class ImServerBoot implements Serializable {

    @Autowired
    private ImServerProperties properties;

    @Autowired(required = false)
    private ImTcpServer tcpServer;
    @Autowired(required = false)
    private ImWsServer wsServer;



    public  void startServer() throws InterruptedException {
        Boolean enableTcp = properties.isEnableTcp();
        Boolean enableWs = properties.isEnableWs();
        Boolean unionServer = properties.isUnionServer();
        Boolean start = this.isStart();
        if (!start){
            return;
        }
//屏蔽掉联合端口
//        if ((unionServer||enableTcp) &&tcpServer!=null){
//            tcpServer.start();
//        }

        // 开启单独端口请注释tcp
        if (enableTcp&&tcpServer!=null){
            tcpServer.start();
        }

        if (enableWs&&wsServer!=null){
            wsServer.start();
        }

    }




    public Boolean  isStart(){
        boolean enableTcp = properties.isEnableTcp();
        boolean enableWs = properties.isEnableWs();
        boolean unionServer = properties.isUnionServer();
        if (!enableTcp&&!enableWs&&!unionServer){
            return false;
        }

// 屏蔽掉联合端口
//        if (!unionServer&&(enableWs&&enableTcp)){
//          log.info("配置无效!同时开启tcp和websocket配置无效,请设置 unionServer=true");
//          return false;
//        }

        return  true;





    }



}



