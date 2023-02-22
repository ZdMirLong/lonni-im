package com.lonni.im.client;

import cn.hutool.core.lang.ConsistentHash;
import com.lonni.im.core.protocol.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * testTcpClient：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 11:32
 */
public class testTcpClient implements Serializable {


    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;





    @Test
    public void start() throws InterruptedException {
        String ip="127.0.0.1"; Integer port=9898;


        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class)
                //4 设置通道的选项参数， 对于服务端而言就是ServerSocketChannel， 客户端而言就是SocketChannel；
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        //加入日志处理
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        //加入编解码期
                        pipeline.addLast(MessageCodec.getInstance());
                        pipeline.addLast(new ClientHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(ip, port)).sync();
        // 增加钩子函数
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                workerGroup.shutdownGracefully();
            }
        }));


        try {
            // 7 监听通道关闭事件
            // 应用程序会一直等待，直到channel关闭
            ChannelFuture closeFuture =
                    channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (
                Exception e) {
            System.out.println("发生其他异常");
            e.printStackTrace();

        } finally {
            // 8 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerGroup.shutdownGracefully();

        }


    }




    @Test
    public void test1(){
        List<String> list=new ArrayList<>();
        list.add("127.0.0.1");
        list.add("127.0.0.2");
        list.add("127.0.0.3");
        list.add("127.0.0.4");
        list.add("127.0.0.5");

        /*
        * 编号为:123的数据所在节点为:127.0.0.1
编号为:124的数据所在节点为:127.0.0.5
编号为:125的数据所在节点为:127.0.0.5
编号为:126的数据所在节点为:127.0.0.2
编号为:127的数据所在节点为:127.0.0.2
编号为:128的数据所在节点为:127.0.0.4
*
*第二次
* 编号为:123的数据所在节点为:127.0.0.1
编号为:124的数据所在节点为:127.0.0.5
编号为:125的数据所在节点为:127.0.0.5
编号为:126的数据所在节点为:127.0.0.2
编号为:127的数据所在节点为:127.0.0.2
编号为:128的数据所在节点为:127.0.0.4


*
*
        *
        *
        *
        * */



        ConsistentHash<String> consistentHash=new ConsistentHash<>(10,list);
        String [] userIds=new String[]{"123","124","125","126","127","128"};

        for (int i = 0; i < userIds.length; i++) {
            String s = consistentHash.get(userIds[i]);
            System.out.println("编号为:"+userIds[i]+"的数据所在节点为:"+s);
        }




    }



}



