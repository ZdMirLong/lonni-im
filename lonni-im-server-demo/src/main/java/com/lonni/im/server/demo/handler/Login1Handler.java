package com.lonni.im.server.demo.handler;

import com.lonni.im.core.action.LoginRequestAction;
import com.lonni.im.core.annotation.MsgHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * LoginHandler：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 11:29
 */
@Component
@Slf4j
@MsgHandler(order = 1)
@ChannelHandler.Sharable
public class Login1Handler extends SimpleChannelInboundHandler<LoginRequestAction> {



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestAction msg) throws Exception {
      log.info("Login1 登录处理器接受到请求:{}",msg.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Login1 进入channelActive 方法");
    }
}



