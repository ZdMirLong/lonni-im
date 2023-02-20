package com.lonni.im.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * IndexController：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/16 0016 17:58
 */
@RestController
public class IndexController implements Serializable {

    @GetMapping("/")
    public String index(){
        return  "hello index";
    }

}



