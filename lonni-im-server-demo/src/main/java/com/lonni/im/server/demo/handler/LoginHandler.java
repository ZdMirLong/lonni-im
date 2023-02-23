//package com.lonni.im.server.demo.handler;
//
//import com.lonni.im.core.action.LoginRequestAction;
//import com.lonni.im.core.annotation.MsgHandler;
//import com.lonni.im.core.util.SpringContext;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.io.Serializable;
//
///**
// * LoginHandler：
// * Description:
// *
// * @author: Lonni
// * @date: 2023/2/17 0017 11:29
// */
//@Component
//@Slf4j
//@MsgHandler(order = 2)
//@ChannelHandler.Sharable
//public class LoginHandler extends SimpleChannelInboundHandler<LoginRequestAction> {
//
//
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestAction msg) throws Exception {
//      log.info("Login登录处理器接受到请求:{}",msg.toString());
//        ctx.fireChannelRead(msg);
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        log.info("Login进入channelActive 方法");
//        super.channelActive(ctx);
//    }
//}
//
//
//
