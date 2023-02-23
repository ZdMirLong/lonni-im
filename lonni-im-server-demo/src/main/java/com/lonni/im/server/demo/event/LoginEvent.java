package com.lonni.im.server.demo.event;

import cn.hutool.core.lang.Singleton;
import com.lonni.im.core.action.ActionEnum;
import com.lonni.im.core.action.LoginRequestAction;
import com.lonni.im.core.action.LoginResponseAction;
import com.lonni.im.core.event.IEvent;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * LoginEvent：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/23 0023 17:54
 */
@Slf4j
public class LoginEvent implements IEvent<LoginRequestAction, LoginResponseAction> {

    public static LoginEvent getInstance() {
        return Singleton.get(LoginEvent.class);
    }

    @Override
    public LoginResponseAction execute(LoginRequestAction requestAction, ChannelHandlerContext ctx) throws InterruptedException {
        log.info("开始处理LoginEvent");
        log.info("处理成功:{}", requestAction.toString());
        Thread.sleep(1000);
        LoginResponseAction responseAction = new LoginResponseAction(true, "denglu wanc ");
        responseAction.setMessageType(ActionEnum.LOGIN_RES.getCode());
        responseAction.setSeriaType(requestAction.getSeriaType());
        return responseAction;
    }
}



