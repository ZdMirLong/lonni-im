package com.lonni.im.fxc;

import com.lonni.im.client.boot.ClientBoot;
import com.lonni.im.core.action.ActionEnum;
import com.lonni.im.core.event.EventBus;
import com.lonni.im.fxc.netty.LoginReponseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * FxcClientApplication：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 12:11
 */
@SpringBootApplication(scanBasePackages = "com.lonni.im")
public class FxcClientApplication implements ApplicationListener<ApplicationStartedEvent> {


    @Autowired
    private ClientBoot clientBoot;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(FxcClientApplication.class, args);
        System.out.println("springboot启动成功");
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        EventBus.getInstance().putEvent(ActionEnum.LOGIN_RES.getCode(), LoginReponseEvent.getInstance());
        System.out.println("启动客户端");
        clientBoot.start();
        System.out.println("启动客户端完成");


    }
}



