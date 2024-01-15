package com.chaos.service;


import com.chaos.response.ResponseResult;
import org.springframework.stereotype.Service;

public interface WeiXinService {
    ResponseResult wxLoginUserDetail(String code);
}
