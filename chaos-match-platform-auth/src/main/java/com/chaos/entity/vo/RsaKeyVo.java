package com.chaos.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: RsaKeyVo
 * @author: xsinxcos
 * @create: 2024-04-01 22:29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsaKeyVo {
    //公钥
    private String publicKey;
    //生成的时间戳
    private Long timeStamp;
}
