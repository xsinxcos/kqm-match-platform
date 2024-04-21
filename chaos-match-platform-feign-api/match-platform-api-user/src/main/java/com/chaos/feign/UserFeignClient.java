package com.chaos.feign;

import com.chaos.feign.bo.AuthUserBo;
import com.chaos.feign.bo.PosterBo;
import com.chaos.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用户模块远程调用
 */

@FeignClient(value = "UserService")
public interface UserFeignClient {
    /**
     * 根据微信OPENID获取用户
     * @param openid
     * @return
     */
    @GetMapping("/feign/getUserByOpenId")
    ResponseResult<AuthUserBo> getUserByOpenId(@RequestParam("openid") String openid);

    /**
     * 根据微信OPENID添加用户
     * @param openid
     * @return
     */
    @PostMapping("/feign/addUserByOpenId")
    ResponseResult<AuthUserBo> addUserByOpenId(@RequestParam("openid") String openid);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    @GetMapping("/feign/getUserByUsername")
    ResponseResult<AuthUserBo> getUserByUsername(@RequestParam("username") String username);

    /**
     * 根据用户ID批量获取用户
     * @param ids
     * @return
     */
    @GetMapping("/feign/getBatchUserByUserIds")
    ResponseResult<Map<Long, PosterBo>> getBatchUserByUserIds(@RequestParam("userIds") List<Long> ids);


    /**
     * 根据用户ID获取用户
     * @param id
     * @return
     */
    @GetMapping("/feign/getUserByUserId")
    ResponseResult<AuthUserBo> getUserById(@RequestParam("id") Long id);

    /**
     * 根据邮箱获取用户
     * @param email
     * @return
     */
    @GetMapping("/feign/getUserByEmail")
    ResponseResult<AuthUserBo> getUserByEmail(@RequestParam("email") String email);
}
