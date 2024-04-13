package com.chaos.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.chaos.emtity.RSAKey;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @description: RSA非对称加密工具类
 * @author: xsinxcos
 * @create: 2024-03-30 16:32
 **/

public class RSAUtils {

    public final static int KEY_TTL = 30;

    public final static TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    public static RSAKey getKeyPair(RSA rsa) {
        //获得私钥
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        //获得公钥
        String publicKeyBase64 = rsa.getPublicKeyBase64();

        RSAKey rsaKey = new RSAKey(publicKeyBase64, privateKeyBase64);

        return rsaKey;
    }

    //加密
    public static String getEncryptString(String str, RSA rsa) {
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(str, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        return Base64.getEncoder().encodeToString(encrypt);
    }

    //解密
    public static String getDecryptString(String str, RSA rsa) {
        byte[] aByte = Base64.getDecoder().decode(str);
        byte[] decrypt = rsa.decrypt(aByte, KeyType.PrivateKey);
        return new String(decrypt, CharsetUtil.CHARSET_UTF_8);
    }
}
