package com.lonni.tio.msg;

import org.tio.core.intf.Packet;

import java.io.Serializable;

/**
 * HelloPacket：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/22 0022 14:21
 */
public class HelloPacket extends Packet {
    private static final long serialVersionUID = -172060606924066412L;
    public static final int HEADER_LENGHT = 4;//消息头的长度
    public static final String CHARSET = "utf-8";
    private byte[] body;
    /**
     * @return the body
     */
    public byte[] getBody() {
        return body;
    }
    /**
     * @param body the body to set
     */
    public void setBody(byte[] body) {
        this.body = body;
    }
}



