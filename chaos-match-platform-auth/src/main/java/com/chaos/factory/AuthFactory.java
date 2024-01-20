package com.chaos.factory;

import com.chaos.strategy.AuthGranterStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 登录授权策略工厂
 * @author: xsinxcos
 * @create: 2024-01-20 23:33
 **/
@Component
public class AuthFactory {
    private final Map<String, AuthGranterStrategy> granterMap = new ConcurrentHashMap<>();

    public AuthFactory(Map<String, AuthGranterStrategy> granterMap){
        this.granterMap.putAll(granterMap);
    }

    public AuthGranterStrategy getGranter(String grantType){
        AuthGranterStrategy granterStrategy = granterMap.get(grantType);
        Optional.ofNullable(granterStrategy).orElseThrow(() -> new RuntimeException("不存在此种登录类型"));
        return granterStrategy;
    }

}
