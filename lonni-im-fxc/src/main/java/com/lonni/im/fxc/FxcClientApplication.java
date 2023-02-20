package com.lonni.im.fxc;

import com.lonni.im.fxc.netty.TcpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.Serializable;

/**
 * FxcClientApplication：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 12:11
 */
@SpringBootApplication(scanBasePackages = "com.lonni.im")
@EnableAsync
public class FxcClientApplication implements Serializable {



    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(FxcClientApplication.class, args);
        TcpClient bean = context.getBean(TcpClient.class);
        bean.start();

    }

}



