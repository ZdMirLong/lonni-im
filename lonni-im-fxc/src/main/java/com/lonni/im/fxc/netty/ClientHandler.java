package com.lonni.im.fxc.netty;

import com.lonni.im.core.action.LoginRequestAction;
import com.lonni.im.core.serializable.SerializeAlgorithmEnum;
import com.lonni.im.core.serializable.SerializerBase;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ClientHandler：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 11:34
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("链接成功");
        super.channelActive(ctx);
//        // 发送数据
//        LoginRequestAction loginAction = new LoginRequestAction();
//        loginAction.setSeriaType(SerializeAlgorithmEnum.JDK.getTypeToInt());
//        loginAction.setPwd("111");
//        loginAction.setUserName("222");
//        ctx.writeAndFlush(loginAction);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("jiehsoudao shuju  "+msg.toString());
        super.channelRead(ctx, msg);
    }
}



