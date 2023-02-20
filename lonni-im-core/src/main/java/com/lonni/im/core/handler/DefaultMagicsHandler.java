package com.lonni.im.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 默认的魔数校验器
 * @author: Lonni
 * @date: 2023/2/16 0016 16:23
 */
@Service
@Slf4j
public class DefaultMagicsHandler implements MagicsHandler {


    @Override
    public Boolean hand(byte[] bytes) {
        log.info("魔数校验器");
        return true;
    }
}



