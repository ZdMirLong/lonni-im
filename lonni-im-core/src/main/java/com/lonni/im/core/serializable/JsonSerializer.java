package com.lonni.im.core.serializable;

import cn.hutool.core.lang.Singleton;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * json 序列化方式
 * 使用 fastJson
 *
 * @author: Lonni
 * @date: 2023/2/7 0007 9:52
 */
public class JsonSerializer implements SerializerBase {
    private final Logger log = LoggerFactory.getLogger(JsonSerializer.class);

    public static SerializerBase getInstance() {
        return Singleton.get(JsonSerializer.class);
    }


    /**
     * 序列化
     *
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) throws IOException {
        String jsonString = JSON.toJSONString(object);
        return jsonString.getBytes(StandardCharsets.UTF_8);
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
        String str = new String(bytes, StandardCharsets.UTF_8);
//        str=str.trim();
        JSONObject jsonObject = JSON.parseObject(str);
        log.info(jsonObject.toJSONString());
        T t1 = JSON.parseObject(str, clazz);
        return t1;
    }
}



