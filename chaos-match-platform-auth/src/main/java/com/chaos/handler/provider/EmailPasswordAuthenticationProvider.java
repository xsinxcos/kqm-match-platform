package com.chaos.handler.provider;

import com.chaos.entity.LoginUser;
import com.chaos.entity.User;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.handler.authenticationToken.EmailPasswordAuthenticationToken;
import com.chaos.response.ResponseResult;
import com.chaos.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserFeignClient userFeignClient;

    private final PasswordEncoder passwordEncoder;

    /**
     * 认证
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        EmailPasswordAuthenticationToken token = (EmailPasswordAuthenticationToken) authentication;

        ResponseResult<AuthUserBo> userByEmail = userFeignClient.getUserByEmail(token.getEmail());
        Optional.ofNullable(userByEmail).orElseThrow(() -> new RuntimeException("用户不存在"));

        AuthUserBo authUserBo = userByEmail.getData();

        boolean matches = passwordEncoder.matches(token.getCredentials(), authUserBo.getPassword());

        if (!matches) {
            throw new RuntimeException("密码错误");
        }
        LoginUser loginUser = new LoginUser(BeanCopyUtils.copyBean(authUserBo, User.class));

        EmailPasswordAuthenticationToken authenticationToken = new EmailPasswordAuthenticationToken(loginUser, token.getCredentials(), null);

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return EmailPasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}