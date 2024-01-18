package com.chaos.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.model.entity.AuthUser;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.mapper.AuthUserMapper;
import com.chaos.response.ResponseResult;
import com.chaos.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserFeignController implements UserFeignClient{
    private final AuthUserMapper authUserMapper;

    @Override
    public ResponseResult getUserByOpenId(String openid) {
        LambdaQueryWrapper<AuthUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(openid) , AuthUser::getOpenid ,openid);
        AuthUserBo bo = BeanCopyUtils.copyBean(authUserMapper.selectOne(wrapper), AuthUserBo.class);
        return ResponseResult.okResult(bo);
    }

    @Override
    public ResponseResult addUserByOpenId(String openid) {
        AuthUser user = new AuthUser();
        user.setOpenid(openid);
        authUserMapper.insert(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserByUsername(String username) {
        AuthUser user = authUserMapper.selectOne(new LambdaQueryWrapper<AuthUser>().eq(
                Objects.nonNull(username),
                AuthUser::getUserName,
                username
        ));
        AuthUserBo bo = BeanCopyUtils.copyBean(user, AuthUserBo.class);
        return ResponseResult.okResult(bo);
    }
}
