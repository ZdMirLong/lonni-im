package com.lonni.im.core.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 登录响应
 * @author lonni
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class LoginResponseAction extends AbstractResponseAction{
    public LoginResponseAction(boolean success, String reason) {
        super(success, reason);
    }
    @Override
    public int getMessageType() {
        return ActionEnum.LOGIN_RES.getCode();
    }
}
