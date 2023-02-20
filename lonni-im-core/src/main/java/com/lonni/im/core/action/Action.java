package com.lonni.im.core.action;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 消息的父类
 * 所有的消息都必须继承此类
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 14:36
 */
@Data
@ToString
public abstract class Action implements Serializable {

    /**
     * 版本 1 字节
     */
    private Integer version = 1;
    /**
     * 序列化类型 1 字节
     */
    private Integer seriaType;


    /**
     * 唯一id
     */
    private Integer sequenceId=10001;
    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 获取消息类型
     *
     * @return
     */
    public abstract int getMessageType();


    public Boolean checkMsg() {
        Assert.notNull(this.getSeriaType(), "序列化类型不能为空!");
        Assert.notNull(this.getMessageType(), "消息类型不能为空!");
        return true;


    }


}



