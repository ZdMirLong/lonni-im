package com.lonni.im.client.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import com.lonni.im.core.action.Action;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

/**
 * ImClient：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/24 0024 11:33
 */
@Slf4j
public class ImClient implements Serializable {

    public static ImClient getInstance() {
        return Singleton.get(ImClient.class);
    }

    private static ChannelFuture CHANNEL_FUTURE = null;

    public void setChannelFuture(ChannelFuture future) {
        CHANNEL_FUTURE = future;
    }

    public void sendMsg(Action msg) {
        if (Objects.isNull(msg)) {
            return;
        }
        if (CHANNEL_FUTURE.channel().isActive()&&CHANNEL_FUTURE.channel().isWritable()){
            CHANNEL_FUTURE.channel().writeAndFlush(msg);
            return;
        }
        log.info("客户端链接处于不可写状态.....");
    }


}



