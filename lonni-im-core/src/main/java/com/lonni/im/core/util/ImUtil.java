package com.lonni.im.core.util;

import cn.hutool.core.lang.Singleton;
import com.lonni.im.core.constains.GloadConstains;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;

/**
 * ImUtil：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/17 0017 9:51
 */
public class ImUtil implements Serializable {
private  final Logger log= LoggerFactory.getLogger(ImUtil.class);

    public static ImUtil getInstance(){
        return Singleton.get(ImUtil.class);
    }


    /**
     * 判断魔数
     * @param data
     * @return
     */
    public synchronized  boolean checkMagics(byte[] data) {
        boolean check = Arrays.equals(data, GloadConstains.magis);
        log.info("魔数判断:" + check);
        return check;
    }


    public  boolean isHttp(int magic1, int magic2) {
        return
                magic1 == 'G' && magic2 == 'E' || // GET
                        magic1 == 'P' && magic2 == 'O' || // POST
                        magic1 == 'P' && magic2 == 'U' || // PUT
                        magic1 == 'H' && magic2 == 'E' || // HEAD
                        magic1 == 'O' && magic2 == 'P' || // OPTIONS
                        magic1 == 'P' && magic2 == 'A' || // PATCH
                        magic1 == 'D' && magic2 == 'E' || // DELETE
                        magic1 == 'T' && magic2 == 'R' || // TRACE
                        magic1 == 'C' && magic2 == 'O';   // CONNECT
    }



}



