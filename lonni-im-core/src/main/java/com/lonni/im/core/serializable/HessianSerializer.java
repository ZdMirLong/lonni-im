package com.lonni.im.core.serializable;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian 序列化方式
 *
 * @author: Lonni
 * @date: 2023/2/7 0007 9:35
 */
public class HessianSerializer implements SerializerBase {
    private final Logger logger = LoggerFactory.getLogger(HessianSerializer.class);

    public static SerializerBase getInstance() {
        return Singleton.get(HessianSerializer.class);
    }

    /**
     * 序列化
     *
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) throws IOException {
        Assert.notNull(object, "the serialize object can not be null");
        byte[] bytes = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(outputStream);
        try {
            hessian2Output.writeObject(object);
            hessian2Output.flush();
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            logger.warn("HessianSerializer [serialize] error, cause:{}", e.getMessage(), e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;

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
        Assert.notNull(bytes, "the deserialize bytes can not be null");
        T object = null;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(inputStream);
        try {
            object = (T) hessian2Input.readObject();
        } catch (IOException e) {
            logger.warn("HessianSerializer [deserialize] error, cause:{}", e.getMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}



