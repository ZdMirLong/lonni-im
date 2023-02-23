package com.lonni.im.server.demo.lister;

import com.lonni.im.core.action.ActionEnum;
import com.lonni.im.core.event.EventBus;
import com.lonni.im.server.demo.event.LoginEvent;
import com.lonni.im.server.server.ImServerBoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * NettyLister：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 12:49
 */
@Component
public class NettyLister implements ApplicationRunner , ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private ImServerBoot serverBoot;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        serverBoot.startServer();


    }

    /**
     * 程序启动成功,向eventBus中注册事件
     * @param applicationStartedEvent
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        EventBus.getInstance().putEvent(ActionEnum.LOGIN_REQ.getCode(), LoginEvent.getInstance());


    }

}



