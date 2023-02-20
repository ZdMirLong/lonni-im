package com.lonni.im.core.handler;

/**
 * 魔数校验器
 * @author: Lonni
 * @date: 2023/2/16 0016 16:22
 */
public interface MagicsHandler {

    /**
     * 检验魔数是否正确
     * @param bytes
     * @return
     */
    Boolean hand(byte[] bytes);

}
