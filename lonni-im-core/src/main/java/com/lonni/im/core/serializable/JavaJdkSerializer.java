package com.lonni.im.core.serializable;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ObjectUtil;

import java.io.IOException;

/**
 * java 序列化方式
 *
 * @author: Lonni
 * @date: 2023/2/7 0007 9:35
 */
public class JavaJdkSerializer implements SerializerBase {

    public static SerializerBase getInstance() {
        return Singleton.get(JavaJdkSerializer.class);
    }
    /**
     * 序列化
     *
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) throws IOException {
        return ObjectUtil.serialize(object);

    }

    /**
     * 反序列化
     *
     * @param bytes
     * @param clazz
     * @return
     */
    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) throws IOException, ClassNotFoundException {
        return (T) ObjectUtil.deserialize(bytes);
    }
}



