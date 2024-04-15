package com.chaos.handler.provider;

import com.chaos.entity.LoginUser;
import com.chaos.entity.User;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.handler.RBACPermissionsHandler;
import com.chaos.handler.authenticationToken.EmailPasswordAuthenticationToken;
import com.chaos.response.ResponseResult;
import com.chaos.util.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Slf4j
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RBACPermissionsHandler rbacPermissionsHandler;

    public EmailPasswordAuthenticationProvider() {
    }


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

        LoginUser loginUser = (LoginUser)rbacPermissionsHandler.setRBACPermissions(BeanCopyUtils.copyBean(authUserBo, User.class));

        EmailPasswordAuthenticationToken authenticationToken = new EmailPasswordAuthenticationToken(loginUser, token.getCredentials(), null);

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return EmailPasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }

}