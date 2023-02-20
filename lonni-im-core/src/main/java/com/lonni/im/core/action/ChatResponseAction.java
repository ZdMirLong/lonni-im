package com.lonni.im.core.action;


import lombok.Data;
import lombok.ToString;

/**
 * 单聊响应体
* @Description:
* @Param:
* @returns:
* @author: lonni
* @Date: 2023/2/16 0016 15:33
*/
@Data
@ToString(callSuper = true)
public class ChatResponseAction extends AbstractResponseAction {

    private String from;
    private String content;

    public ChatResponseAction(boolean success, String reason) {
        super(success, reason);
    }

    public ChatResponseAction(String from, String content) {
        this.from = from;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return ActionEnum.CHAT_RES.getCode();
    }
}
