package com.lonni.tio.msg;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.tio.core.intf.Packet;

/**
 * ResponsePackage：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/22 0022 11:38
 */
@Data
public class ResponsePackage extends Packet {
    private static final long serialVersionUID = -172060606924066412L;
    public static final String CHARSET = "utf-8";
    private JSONObject body;
    private String phoneNum;
    private Integer type; // 下发指令类型

}



