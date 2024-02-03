package com.chaos.filter;

import com.alibaba.fastjson.JSON;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.constant.LoginConstant;
import com.chaos.entity.LoginUser;
import com.chaos.response.ResponseResult;
import com.chaos.util.JwtUtil;
import com.chaos.util.RedisCache;
import com.chaos.util.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = request.getHeader("access_token");
        if (!StringUtils.hasText(token)) {
            //说明该接口不需要登录，直接放行
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = null;
        //解析获取userid
        try {
            claims = JwtUtil.parseShortToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时 token非法
            //响应告诉前端需要重新登录或者重新获取assessToken
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = JSON.parseObject(redisCache.getCacheObject(
                LoginConstant.USER_REDIS_PREFIX + userId), LoginUser.class);
        //如果获取不到
        if (Objects.isNull(loginUser)) {
            //说明登录过期  提示重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
