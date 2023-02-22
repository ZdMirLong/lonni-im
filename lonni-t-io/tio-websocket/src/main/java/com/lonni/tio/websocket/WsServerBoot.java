package com.lonni.tio.websocket;

import com.lonni.tio.websocket.config.ShowcaseServerConfig;
import com.lonni.tio.websocket.handler.ServerAioListenerImpl;
import com.lonni.tio.websocket.handler.ShowcaseIpStatListener;
import com.lonni.tio.websocket.handler.ShowcaseWsMsgHandler;
import org.tio.server.ServerTioConfig;
import org.tio.utils.jfinal.P;
import org.tio.websocket.server.WsServerStarter;

import java.io.Serializable;

/**
 * WsServerBoot：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/22 0022 10:56
 */

public class WsServerBoot implements Serializable {

    /**
     * ws的启动类
     */
    private WsServerStarter wsServerStarter;
    /**
     * 配置
     */
    private ServerTioConfig serverTioConfig;


    public WsServerBoot(int port) throws Exception {
        //初始化ws服务 ,传入握手类
        wsServerStarter = new WsServerStarter(port, new ShowcaseWsMsgHandler());
        //拿到服务配置
       serverTioConfig = wsServerStarter.getServerTioConfig();
        //设置监听器
       serverTioConfig.setServerAioListener(ServerAioListenerImpl.me);
        //如果有handler的化 增加自定义的handler
        //serverTioConfig =   new  ServerTioConfig(null,ServerAioListenerImpl.me);

        //设置协议名字
        serverTioConfig.setName(ShowcaseServerConfig.PROTOCOL_NAME);

//设置ip监控
        serverTioConfig.setIpStatListener(ShowcaseIpStatListener.me);



        //设置ip统计时间段
        serverTioConfig.ipStats.addDurations(ShowcaseServerConfig.IpStatDuration.IPSTAT_DURATIONS);
        //设置心跳超时时间
        serverTioConfig.setHeartbeatTimeout(ShowcaseServerConfig.HEARTBEAT_TIMEOUT);
        if (P.getInt("ws.use.ssl", 1) == 1) {
            //如果你希望通过wss来访问，就加上下面的代码吧，不过首先你得有SSL证书（证书必须和域名相匹配，否则可能访问不了ssl）
//			String keyStoreFile = "classpath:config/ssl/keystore.jks";
//			String trustStoreFile = "classpath:config/ssl/keystore.jks";
//			String keyStorePwd = "214323428310224";


            String keyStoreFile = P.get("ssl.keystore", null);
            String trustStoreFile = P.get("ssl.truststore", null);
            String keyStorePwd = P.get("ssl.pwd", null);
            serverTioConfig.useSsl(keyStoreFile, trustStoreFile, keyStorePwd);
        }
    }

    public static void start() throws Exception {
        WsServerBoot wsServerBoot = new WsServerBoot(ShowcaseServerConfig.SERVER_PORT);
        wsServerBoot.wsServerStarter.start();
    }

    /**
     * @return the serverTioConfig
     */
    public ServerTioConfig getServerTioConfig() {
        return serverTioConfig;
    }

    public WsServerStarter getWsServerStarter() {
        return wsServerStarter;
    }

}



