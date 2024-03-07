package com.chaos.aspect;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.entity.User;
import com.chaos.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 权限校验切面
 * @author: xsinxcos
 * @create: 2024-03-06 20:18
 **/
@Component
@Aspect
@Slf4j
public class AuthAdminAspect {
    private static final Integer ADMIN_USER_STATUS = 1;

    @Pointcut("@annotation(com.chaos.annotation.AuthAdminCheck)")
    public void pt() {
    }

    @Before("pt()")
    public void checkAuth(JoinPoint joinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
        }

        //获取被增强方法上的注解对象
        AuthAdminCheck authAdminCheck = getAuthCheck(joinPoint);

        User user = SecurityUtils.getLoginUser().getUser();

        if (!ADMIN_USER_STATUS.equals(user.getType())) {
            throw new RuntimeException(AppHttpCodeEnum.NO_OPERATOR_AUTH.getMsg());
        }

    }


    private AuthAdminCheck getAuthCheck(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AuthAdminCheck authAdminCheck = methodSignature.getMethod().getAnnotation(AuthAdminCheck.class);
        return authAdminCheck;
    }
}
