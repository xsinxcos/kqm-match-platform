package com.chaos.feign;

import com.chaos.feign.bo.AuthUserBo;
import com.chaos.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "UserService")
public interface UserFeignClient {
    @GetMapping("/feign/getUserById")
    ResponseResult<AuthUserBo> getUserByOpenId(@RequestParam("openid") String openid);

    @PostMapping("/feign/addUser")
    ResponseResult<AuthUserBo> addUserByOpenId(@RequestParam("openid") String openid);

    @GetMapping("/feign/getUserByUsername")
    ResponseResult<AuthUserBo> getUserByUsername(@RequestParam("username") String username);
}
