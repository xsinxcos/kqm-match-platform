package com.chaos.config;

import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@Getter
@Component
@ConfigurationProperties(prefix = "wechat.open")
public class WechatOpenProperties {
    /**
     * 设置微信三方平台的appid
     */
    private String componentAppId;

    /**
     * 设置微信三方平台的app secret
     */
    private String componentSecret;

    /**
     * 设置微信三方平台的token
     */
    private String componentToken;

    /**
     * 设置微信三方平台的EncodingAESKey
     */
    private String componentAesKey;

    public void setComponentAppId(String componentAppId) {
        this.componentAppId = componentAppId;
    }

    public void setComponentSecret(String componentSecret) {
        this.componentSecret = componentSecret;
    }

    public void setComponentToken(String componentToken) {
        this.componentToken = componentToken;
    }

    public void setComponentAesKey(String componentAesKey) {
        this.componentAesKey = componentAesKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
