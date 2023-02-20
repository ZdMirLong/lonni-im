package com.lonni.im.core.action;

import lombok.Getter;

/**
 * ActionEnum：
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 14:40
 */
public enum ActionEnum {
    /**
     * 登录消息类型
     */
    LOGIN_REQ(1,"登录请求"),
    /**
     * 登录响应
     */
    LOGIN_RES(2,"登录响应"),
    /**
     * 心跳请求
     */
    PING(3,"ping"),
    /**
     * 心跳响应
     */
    PONG(4,"pong"),
    /**
     * 单聊消息请求
     */
    CHAT_REQ(5,"单聊消息请求"),
    /**
     * 单聊消息响应
     */
    CHAT_RES(6,"单聊消息响应"),
    /**
     * 群聊消息请求
     */
    GROUP_CHAT_REQ(7,"群聊消息请求"),
    /**
     * 群聊消息响应
     */
    GROUP_CHAT_RES(8,"群聊消息响应"),



;
    @Getter
    private int code;

    @Getter
    private String desc;

    ActionEnum(final int action, final String desc) {
        this.code = action;
        this.desc = desc;
    }


}
