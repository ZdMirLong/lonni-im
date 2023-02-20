package com.lonni.im.core.serializable;

import java.io.IOException;

/**
 * 序列化接口,所有的序列化都要实现该接口
 * 全使用 utf-8编码
 *
 * @author: Lonni
 * @date: 2023/2/7 0007 9:33
 */
public interface SerializerBase {


    /**
     * 序列化
     * @param object
     * @return
     */
    public byte[] serialize (Object object) throws IOException;

    /**
     * 反序列化
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T deSerialize (byte[] bytes,Class<T>  clazz) throws IOException, ClassNotFoundException;
}
