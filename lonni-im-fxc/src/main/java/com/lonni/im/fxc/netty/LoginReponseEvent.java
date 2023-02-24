package com.lonni.im.fxc.netty;

import cn.hutool.core.lang.Singleton;
import com.lonni.im.core.action.LoginResponseAction;
import com.lonni.im.core.event.IEvent;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.zip.Inflater;

/**
 * LoginReponseEvent：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 15:16
 */
@Slf4j
public class LoginReponseEvent implements IEvent<LoginResponseAction, Boolean> {
    public static LoginReponseEvent getInstance() {
        return Singleton.get(LoginReponseEvent.class);
    }

    @Override
    public Boolean execute(LoginResponseAction requestAction, ChannelHandlerContext ctx) throws InterruptedException {
        log.info("接受到数据:{}", requestAction.toString());
        Thread.sleep(1000);
        return null;
    }
}



