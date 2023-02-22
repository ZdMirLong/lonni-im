package com.lonni.tio.tcp.boot;

import com.lonni.tio.tcp.handler.HelloServerAioHandler;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;
import java.io.Serializable;

/**
 * ServerBoot：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/22 0022 14:27
 */
public class ServerBoot implements Serializable {
    //handler, 包括编码、解码、消息处理
    public static ServerAioHandler aioHandler = new HelloServerAioHandler();
    //事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
    public static ServerAioListener aioListener = null;
    //一组连接共用的上下文对象
    public static ServerTioConfig serverTioConfig = new ServerTioConfig("hello-tio-server", aioHandler, aioListener);
    //tioServer对象
    public static TioServer tioServer = new TioServer(serverTioConfig);
    //有时候需要绑定ip，不需要则null
    public static String serverIp = "127.0.0.1";
    //监听的端口
    public static int serverPort = 9776;
    /**
     * 启动程序入口
     */
    public static void main(String[] args) throws IOException {
        serverTioConfig.setHeartbeatTimeout(5000);
        tioServer.start(serverIp, serverPort);
    }

}



