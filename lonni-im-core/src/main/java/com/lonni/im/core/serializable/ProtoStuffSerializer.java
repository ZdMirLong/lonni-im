package com.lonni.im.core.serializable;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ReflectUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  <h2>ProtoStuff 序列化方式</h2>
 *  <p>
 *      好处: 不用去编辑proto文件
 *            直接可以利用现有的实体类来通信，而且性能性能损失也是很少的。
 *  </p>
 *
 * @author: Lonni
 * @date: 2023/2/7 0007 9:35
 */
public class ProtoStuffSerializer implements SerializerBase {
    private final Logger logger = LoggerFactory.getLogger(ProtoStuffSerializer.class);

    public static SerializerBase getInstance() {
        return Singleton.get(ProtoStuffSerializer.class);
    }
   //同步锁
    private final  Object LOCK=new Object();
    // 缓存Schema
    private Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();


    /**
     * 获取Schema
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
        if (schema == null) {
            synchronized (LOCK) {
                schema = (Schema<T>) cachedSchema.get(clazz);
                if (schema == null) {
                    schema = RuntimeSchema.getSchema(clazz);
                    cachedSchema.putIfAbsent(clazz, schema);
                }
            }
        }
        return schema;
    }



    /**
     * 序列化
     *
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) throws IOException {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] bytes = null;
        try {
            Class clazz = object.getClass();
            Schema schema = getSchema(clazz);
            bytes = ProtostuffIOUtil.toByteArray(object, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
        return bytes;
    }

    /**
     * 反序列化
     * @param bytes
     * @param clazz
     * @return
     */
    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) throws IOException, ClassNotFoundException {
        T object = null;
        try {
            object = ReflectUtil.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(bytes, object, schema);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return object;
    }
}



