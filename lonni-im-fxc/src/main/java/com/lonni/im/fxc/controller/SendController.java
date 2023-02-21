package com.lonni.im.fxc.controller;

import com.lonni.im.core.action.ChatRequestAction;
import com.lonni.im.core.action.LoginRequestAction;
import com.lonni.im.core.serializable.SerializeAlgorithmEnum;
import com.lonni.im.fxc.netty.TcpClient;
import io.netty.channel.ChannelFuture;
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

    @GetMapping("send1")
    public String  send1(){
        ChannelFuture channelFuture = tcpClient.getChannelFuture();
        ChatRequestAction loginAction = new ChatRequestAction();
        loginAction.setSeriaType(SerializeAlgorithmEnum.JDK.getTypeToInt());
        loginAction.setFrom("1");
        loginAction.setTo("111");
        channelFuture.channel().writeAndFlush(loginAction);
        return  "发送ChatRequestAction成功!";
    }
}



