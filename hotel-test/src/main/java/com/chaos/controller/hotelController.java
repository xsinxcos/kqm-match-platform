package com.chaos.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class hotelController {
    @GetMapping("/hotel")
    public String test(){
        System.out.println("111");
        return "111";
    }
}
