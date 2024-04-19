package com.chaos.utils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @description: 验证码工具类
 * @author: xsinxcos
 * @create: 2024-04-18 22:11
 **/
public class VerifyCodeUtils {
    public static final String CACHE_REGISTER_CODE_PREFIX = "register_code_email:";

    public static final String CACHE_PASSWORD_FORGET_CODE_PREFIX = "password_forget_code_email:";

    public static final int timeOutToCode = 60 * 10;

    public static final int timeOutToSend = 60;

    public static final TimeUnit timeUnit = TimeUnit.SECONDS;
    // 生成6位随机验证码
    /**
     * 生产的验证码位数
     */
    private static final int generateVerificationCodeLength = 6;

    private static final String[] metaCode={"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    public static String generateVerificationCode() {
        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder();
        while (verificationCode.length()<generateVerificationCodeLength){
            int i = random.nextInt(metaCode.length);
            verificationCode.append(metaCode[i]);
        }
        return verificationCode.toString();
    }
}
