package com.lonni.im.core.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * LoginRequestAction：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 15:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class LoginRequestAction extends Action {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String pwd;
    /**
     * token
     */
    private String token;

    @Override
    public int getMessageType() {
        return ActionEnum.LOGIN_REQ.getCode();
    }
}



