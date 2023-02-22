package com.lonni.tio.msg;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tio.core.intf.Packet;

import java.util.List;

/**
 * 消息体
 * @author: Lonni
 * @date: 2023/2/22 0022 10:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MindPackage extends Packet    {
    public static final String CHARSET = "utf-8";
    private static final long serialVersionUID = 6405529188953865257L;
    private List<JSONObject> body;
    private String phoneNum;
    // 下发指令类型
    private Integer type;

}



