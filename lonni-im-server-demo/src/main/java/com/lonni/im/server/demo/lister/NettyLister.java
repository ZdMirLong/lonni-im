package com.lonni.im.server.demo.lister;

import com.lonni.im.server.server.ImServerBoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * NettyLister：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 12:49
 */
@Component
public class NettyLister implements ApplicationRunner {

    @Autowired
    private ImServerBoot serverBoot;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        serverBoot.startServer();


    }
}



