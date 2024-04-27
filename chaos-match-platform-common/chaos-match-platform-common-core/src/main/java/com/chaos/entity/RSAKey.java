package com.chaos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: RSA 密钥对
 * @author: xsinxcos
 * @create: 2024-04-01 22:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RSAKey {
    //公钥
    private String publicKey;

    //私钥
    private String privateKey;
}
