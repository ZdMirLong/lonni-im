package com.lonni.im.client.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * ClientBoot：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 11:26
 */

@Component
@Slf4j
public class ClientBoot implements Serializable {

    @Autowired(required = false)
    private WsClient wsClient;

    @Autowired(required = false)
    private  TcpClient tcpClient;


    public  void   start(){
        if (wsClient!=null){
            wsClient.start();
        }
        if (tcpClient!=null){
            tcpClient.start();
        }

    }



}



