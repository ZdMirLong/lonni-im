package com.lonni.im.core.action;

import cn.hutool.core.lang.Singleton;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息动作操作类
 * @author: Lonni
 * @date: 2023/2/16 0016 14:52
 */
public class ActionFactory implements Serializable {
    /**
     * 存储操作枚举和具体类的缓存
     */
    private static final Map<Integer, Class<? extends Action>> actionMap = new ConcurrentHashMap<>();
    static {
        actionMap.putIfAbsent(ActionEnum.LOGIN_REQ.getCode(), LoginRequestAction.class);
        actionMap.putIfAbsent(ActionEnum.LOGIN_RES.getCode(), LoginResponseAction.class);
        actionMap.putIfAbsent(ActionEnum.PING.getCode(),  PingAction.class);
        actionMap.putIfAbsent(ActionEnum.CHAT_REQ.getCode(),  ChatRequestAction.class);
        actionMap.putIfAbsent(ActionEnum.CHAT_RES.getCode(),  ChatResponseAction.class);
        actionMap.putIfAbsent(ActionEnum.GROUP_CHAT_REQ.getCode(),  GroupChatRequestAction.class);
        actionMap.putIfAbsent(ActionEnum.GROUP_CHAT_RES.getCode(),  GroupChatResponseAction.class);
    }


    /**
     * 获取单例
     * @return
     */
    public static  ActionFactory getInstance(){
        return Singleton.get(ActionFactory.class);
    }


    /**
     * 将枚举和处理类型绑定
     * @param actionEnum
     * @param clazz
     */
    public void addAction(ActionEnum actionEnum,Class<? extends  Action> clazz){
        actionMap.putIfAbsent(actionEnum.getCode(), clazz);
    }


    /**
     * 将枚举和处理类型绑定
     * @param code
     * @param clazz
     */
    public void addAction(Integer code,Class<? extends  Action> clazz){
        actionMap.putIfAbsent(code, clazz);
    }

    /**
     * 根据枚举获取具体的处理类型
     * @param actionEnum
     * @return
     */
    public Class<? extends  Action> getAction(ActionEnum actionEnum){
        return actionMap.getOrDefault(actionEnum.getCode(),null);
    }
    public Class<? extends  Action> getAction(Integer code){
        return actionMap.getOrDefault(code,null);
    }




























}



