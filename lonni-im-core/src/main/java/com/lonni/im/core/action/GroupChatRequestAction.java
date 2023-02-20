package com.lonni.im.core.action;

import lombok.Data;
import lombok.ToString;

/** 群聊消息体
* @Description:
* @Param:
* @returns:
* @author: lonni
* @Date: 2023/2/16 0016 15:34
*/
@Data
@ToString(callSuper = true)
public class GroupChatRequestAction extends Action {
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

    public GroupChatRequestAction(String from, String content,String groupId) {
        this.content = content;
        this.from = from;
        this.groupId=groupId;
    }

    @Override
    public int getMessageType() {
        return ActionEnum.GROUP_CHAT_REQ.getCode();
    }
}
