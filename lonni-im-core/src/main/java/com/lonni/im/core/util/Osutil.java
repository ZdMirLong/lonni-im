package com.lonni.im.core.util;

import java.io.Serializable;

/**
 * Osutil：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/23 0023 15:47
 */
public class Osutil implements Serializable {

    /**
     * 是否是liunx系统
     * @return
     */
    public static boolean isLinux(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("linux");
    }

}



