package com.lonni.im.fxc.controller;

import com.lonni.im.fxc.netty.TcpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * SendController：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 12:21
 */
@RestController
@RequestMapping("im")
public class SendController implements Serializable {

    @Autowired
    private TcpClient tcpClient;
    @GetMapping("send")
    public String  send(){
        tcpClient.sendMessage();
        return  "发送成功!";
    }

}



