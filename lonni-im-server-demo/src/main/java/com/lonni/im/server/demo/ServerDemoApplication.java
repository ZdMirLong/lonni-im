package com.lonni.im.server.demo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * NettyServerApplication：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 19:21
 */
@SpringBootApplication(scanBasePackages = {"com.lonni.im"})
@Slf4j
@EnableAsync
public class ServerDemoApplication  {



    @SneakyThrows
    public static void main(String[] args) {
         SpringApplication.run(ServerDemoApplication.class, args);
        log.info("springboot程序启动成功");

    }



}



