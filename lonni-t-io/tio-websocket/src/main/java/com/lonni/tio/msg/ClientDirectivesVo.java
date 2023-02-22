package com.lonni.tio.msg;

import lombok.Data;

import java.io.Serializable;

/**
 * ClientDirectivesVo：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/22 0022 11:42
 */
@Data
public class ClientDirectivesVo implements Serializable {
    // 结束上报指令
    public static final int END_REPORT_RESPONSE = 0;
    // 心跳检查指令
    public static final int HEART_BEET_REQUEST = 1;
    // GPS开始上报指令
    public static final int GPS_START_REPORT_RESPONSE = 2;
    // 客户端数据下发
    public static final int DATA_DISTRIBUTION = 3;


    // 0:结束上报指令，1:心跳检测指令，2：GPS开始上报指令,3:客户端数据下发
    private Integer type;


}



