package com.lonni.tio;

import com.lonni.tio.websocket.WsServerBoot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tio.utils.jfinal.P;

/**
 * TioWsApplication：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/22 0022 11:09
 */
@SpringBootApplication
public class TioWsApplication implements CommandLineRunner {
    public static void main(String[] args) {

        //是拿classpath下的文件
        P.use("app.properties");
        SpringApplication.run(TioWsApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始启动tio -ws服务");
        WsServerBoot.start();
        System.out.println("启动tio -ws服务完成");

    }
}



