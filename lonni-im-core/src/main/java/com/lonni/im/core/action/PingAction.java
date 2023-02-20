package com.lonni.im.core.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
* @Description:
* @author: lonni
* @Date: 2023/2/16 0016 15:26
*/
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class PingAction extends Action {
    @Override
    public int getMessageType() {
        return ActionEnum.PING.getCode();
    }
}
