package com.chaos.constant;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(401, "需要登录后操作"),
    TOKEN_EXPIRED(402, "无效TOKEN"),
    NO_OPERATOR_AUTH(403, "无权限操作"),

    LOGIN_ERROR(404, "登录失败"),
    WEIXIN_LOGIN_FAIL(405, "微信登录失败"),
    TOKEN_REFRESH_FAIL(406, "续签失败，请重新登录"),
    SYSTEM_ERROR(500, "出现错误"),
    EMPTY_JSCODE(501, "empty jscode"),
    FILE_TYPE_ERROR(502, "文件类型错误"),
    MESSAGE_SEND_FAIL(505, "消息发送失败"),
    ERROR(500, "操作失败"),
    CONTENT_NOT_NULL(504, "评论内容不能为空"), REQUIRE_USERNAME(506, "必需填写用户名"),

    PARAM_ERROR(506 ,"参数异常"),
    MENU_PARENT_ERROR(515, "上级菜单不能修改成自己"),
    SUBMENU_EXIST(516,"存在子菜单不允许删除");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
