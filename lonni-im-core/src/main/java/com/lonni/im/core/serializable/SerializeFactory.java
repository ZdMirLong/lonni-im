package com.lonni.im.core.serializable;

import cn.hutool.core.lang.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取序列化算法
 *
 * @author: Lonni
 * @date: 2023/2/7 0007 10:26
 */
public class SerializeFactory implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(SerializeFactory.class);

    private static ConcurrentHashMap<Integer,SerializerBase> SER_MAP=new ConcurrentHashMap<>();


    public static SerializeFactory getInstance() {
        return Singleton.get(SerializeFactory.class);
    }

    /**
     * 添加自定义的算法
     * 添加到map中
     * @param code
     * @param serializerBase
     */
    public void addSerializeAlgorithm(Integer code,SerializerBase serializerBase){
        SER_MAP.putIfAbsent(code,serializerBase);
    }

    /**
     * 移除自定义的序列化算法
     * 系统自带的无法移除
     * @param code
     */
    public void delSerializeAlgorithm(Integer code){
        SER_MAP.remove(code);
    }

    /**
     * 获取序列化算法
     * @param type
     * @return
     */
    public SerializerBase chooseSerializer(byte type) {
        //1: 先从枚举中获取
        SerializeAlgorithmEnum anEnum = SerializeAlgorithmEnum.getEnum(type);
        // 如果自带的算法不存在,从map中获取
        if (anEnum == null) {
            return SER_MAP.getOrDefault((int) type, null);
        }
        switch (anEnum) {
            case JDK: {
                return JavaJdkSerializer.getInstance();
            }
            case GSON_JSON: {
                return JsonSerializer.getInstance();
            }
            case HESSIAN: {
                return HessianSerializer.getInstance();
            }
            case KRYO: {
                return null;
            }
            case PROTO_STUFF: {
                return ProtoStuffSerializer.getInstance();
            }
            default: {
                return null;
            }
        }


    }


}



