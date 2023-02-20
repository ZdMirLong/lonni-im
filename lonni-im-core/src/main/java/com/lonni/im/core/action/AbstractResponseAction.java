package com.lonni.im.core.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 响应的抽象类
 * @author: Lonni
 * @date: 2023/2/16 0016 15:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public abstract class AbstractResponseAction extends Action {

    private Boolean success;

    private String reason;

    public AbstractResponseAction() {
    }

    public AbstractResponseAction(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }

}



