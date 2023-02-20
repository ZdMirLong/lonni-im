package com.lonni.im.core.action;

import lombok.Data;
import lombok.ToString;

/**
 * 单聊消息体
* @Description:
* @Param:
* @returns:
* @author: lonni
* @Date: 2023/2/16 0016 15:32
*/
@Data
@ToString(callSuper = true)
public class ChatRequestAction extends Action {
    private String content;
    private String to;
    private String from;

    public ChatRequestAction() {
    }

    public ChatRequestAction(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return ActionEnum.CHAT_REQ.getCode();
    }
}
