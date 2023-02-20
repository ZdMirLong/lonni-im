package com.lonni.im.core.action;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupChatResponseAction extends AbstractResponseAction {
    /**
     * 内容
     */
    private String content;
    /**
     * 群id
     */
    private String groupId;
    /**
     * 来自谁
     */
    private String from;

    public GroupChatResponseAction(boolean success, String reason) {
        super(success, reason);
    }

    public GroupChatResponseAction(String from, String content,String groupId) {
        this.from = from;
        this.content = content;
        this.groupId=groupId;
    }
    @Override
    public int getMessageType() {
        return ActionEnum.GROUP_CHAT_RES.getCode();
    }
}
