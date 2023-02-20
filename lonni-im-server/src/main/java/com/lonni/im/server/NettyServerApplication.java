package com.lonni.im.server;

import com.lonni.im.server.model.ImServerProperties;
import com.lonni.im.server.server.ImServerBoot;
import com.lonni.im.server.server.ImTcpServer;
import com.lonni.im.server.server.ImWsServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.Serializable;

/**
 * NettyServerApplication：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 19:21
 */
@SpringBootApplication(scanBasePackages = {"com.lonni.im"})
@EnableConfigurationProperties(ImServerProperties.class)
@Slf4j
@EnableAsync
public class NettyServerApplication  {



    @SneakyThrows
    public static void main(String[] args) {
         SpringApplication.run(NettyServerApplication.class, args);
        log.info("springboot程序启动成功");

    }



}



