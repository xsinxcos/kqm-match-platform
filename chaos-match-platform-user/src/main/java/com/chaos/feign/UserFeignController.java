package com.chaos.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.entity.AuthUser;
import com.chaos.mapper.AuthUserMapper;
import com.chaos.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserFeignController implements UserFeignClient{

}
