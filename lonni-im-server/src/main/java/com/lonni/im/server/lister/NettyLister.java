package com.lonni.im.server.lister;

import com.lonni.im.core.util.SpringContext;
import com.lonni.im.server.handle.ProtocolDispatcher;
import com.lonni.im.server.init.ImTcpServerInitializer;
import com.lonni.im.server.server.ImServerBoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;

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
        ImTcpServerInitializer bean = SpringContext.getBean(ImTcpServerInitializer.class);
        System.out.println(bean);
        ProtocolDispatcher bean1 = SpringContext.getBean(ProtocolDispatcher.class);
        System.out.println(bean1);

        serverBoot.startServer();


    }
}



