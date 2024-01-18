package com.chaos.service;


import com.chaos.response.ResponseResult;

public interface WeiXinService {
    ResponseResult wxLoginUserDetail(String code);
}
