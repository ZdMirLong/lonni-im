package com.lonni.im.core.annotation;

import java.lang.annotation.*;

/**
 * 标识当前类是 netty的处理通道
 * @author: Lonni
 * @date: 2023/2/17 0017 16:56
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MsgHandler {
    /**
     * 顺序字段
     * @return
     */
    int order() default 0;

    /**
     * 单例还是多例  0 单例  1 多例
     * @return
     */
    int scope() default 0;

}
