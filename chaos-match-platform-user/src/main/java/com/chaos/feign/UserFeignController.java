package com.chaos.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.feign.bo.PosterBo;
import com.chaos.mapper.user.AuthUserMapper;
import com.chaos.domain.user.entity.AuthUser;
import com.chaos.response.ResponseResult;
import com.chaos.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserFeignController implements UserFeignClient {
    private final AuthUserMapper authUserMapper;

    @Override
    public ResponseResult getUserByOpenId(String openid) {
        LambdaQueryWrapper<AuthUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(openid), AuthUser::getOpenid, openid);

        AuthUser authUser = authUserMapper.selectOne(wrapper);

        if (Objects.isNull(authUser)) {
            return ResponseResult.okResult();
        }

        AuthUserBo bo = BeanCopyUtils.copyBean(authUser, AuthUserBo.class);
        return ResponseResult.okResult(bo);
    }

    @Override
    public ResponseResult addUserByOpenId(String openid) {
        AuthUser user = new AuthUser();
        user.setOpenid(openid);
        authUserMapper.insert(user);
        AuthUserBo bo = BeanCopyUtils.copyBean(user, AuthUserBo.class);
        return ResponseResult.okResult(bo);
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

    @Override
    public ResponseResult getBatchUserByUserIds(List<Long> ids) {

        if(ids.isEmpty()) return ResponseResult.okResult(new HashMap<>());

        List<AuthUser> authUsers = authUserMapper.selectBatchIds(ids);
        List<PosterBo> posterBos = BeanCopyUtils.copyBeanList(authUsers, PosterBo.class);
        Map<Long, PosterBo> posterBoMap = posterBos.stream()
                .collect(Collectors.toMap(PosterBo::getId, t -> t, (oldValue, newValue) -> oldValue));

        return ResponseResult.okResult(posterBoMap);
    }

    @Override
    public ResponseResult getUserById(Long id) {
        AuthUser user = authUserMapper.selectById(id);
        AuthUserBo bo = BeanCopyUtils.copyBean(user, AuthUserBo.class);
        return ResponseResult.okResult(bo);
    }

    @Override
    public ResponseResult getUserByEmail(String email) {
        AuthUser user = authUserMapper.selectOne(new LambdaQueryWrapper<AuthUser>().eq(
                Objects.nonNull(email),
                AuthUser::getEmail,
                email
        ));
        AuthUserBo bo = BeanCopyUtils.copyBean(user, AuthUserBo.class);
        return ResponseResult.okResult(bo);
    }
}
