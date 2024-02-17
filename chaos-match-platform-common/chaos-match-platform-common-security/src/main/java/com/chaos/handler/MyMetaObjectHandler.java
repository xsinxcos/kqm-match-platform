package com.chaos.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.chaos.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = null;
        try {
            userId = SecurityUtils.getLoginUser().getUser().getId();
        } catch (Exception e) {
            userId = -1L;//表示是自己创建
        }
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        if (!Objects.isNull(SecurityUtils.getLoginUser())) {
            long userId = SecurityUtils.getLoginUser().getUser().getId();
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", userId, metaObject);
        }
    }
}