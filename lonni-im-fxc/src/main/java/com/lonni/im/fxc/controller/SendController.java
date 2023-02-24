package com.lonni.im.fxc.controller;


import com.lonni.im.client.util.ImClient;
import com.lonni.im.core.action.ChatRequestAction;
import com.lonni.im.core.action.LoginRequestAction;
import com.lonni.im.core.serializable.SerializeAlgorithmEnum;
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


    @GetMapping("send")
    public String send() {

        return "发送成功!";
    }

    @GetMapping("send1")
    public String send1() {
        LoginRequestAction loginAction = new LoginRequestAction();
        loginAction.setSeriaType(SerializeAlgorithmEnum.JDK.getTypeToInt());
        loginAction.setUserName("11");
        loginAction.setPwd("111122141");
        ImClient.getInstance().sendMsg(loginAction);
        return "发送LoginRequestAction成功!";
    }
}



