package com.chaos.handler;

import com.chaos.entity.LoginUser;
import com.chaos.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-04-14 11:39
 **/
@Component
public class RBACPermissionsHandler {
    public UserDetails setRBACPermissions(User user) {
        //todo 权限封装
//        if(user.getType().equals(SystemConstants.ADMIN)){
//            List<String> list = menuMapper.selectPermsByUserId(user.getId());
//            return new LoginUser(user ,list);
//        }
        return new LoginUser(user, null);
    }
}
