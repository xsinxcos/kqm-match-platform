package com.chaos.utils;

import com.chaos.domain.XfChatRequest;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: 星火模型工具类
 * @author: xsinxcos
 * @create: 2024-01-31 02:53
 **/
@Component
public class XfGPTUtils {
    // 地址与鉴权信息  https://spark-api.xf-yun.com/v1.1/chat   1.5地址  domain参数为general
    // 地址与鉴权信息  https://spark-api.xf-yun.com/v2.1/chat   2.0地址  domain参数为generalv2
    private static String hostUrl;

    private static String appid;

    private static String apiSecret;

    private static String apiKey;

    @Value("${xfGPT.hostUrl}")
    public void setHostUrl(String hostUrl) {
        XfGPTUtils.hostUrl = hostUrl;
    }

    @Value("${xfGPT.appid}")
    public void setAppid(String appid) {
        XfGPTUtils.appid = appid;
    }

    @Value("${xfGPT.apiSecret}")
    public void setApiSecret(String apiSecret) {
        XfGPTUtils.apiSecret = apiSecret;
    }

    @Value("${xfGPT.apiKey}")
    public void setApiKey(String apiKey) {
        XfGPTUtils.apiKey = apiKey;
    }


    // 鉴权方法
    public static String getAuthUrl() throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).
                addQueryParameter("date", date).
                addQueryParameter("host", url.getHost()).
                build();

        // System.err.println(httpUrl.toString());
        return httpUrl.toString();
    }


    /**
     * 获取Header
     *
     * @return XfChatRequest.Header
     */
    private static XfChatRequest.Header getHeader() {
        return new XfChatRequest.Header(appid, UUID.randomUUID().toString().substring(0, 10));
    }

    /**
     * 获取Parameter
     *
     * @return XfChatRequest.Parameter
     */
    private static XfChatRequest.Parameter getParameter() {
        return new XfChatRequest.Parameter(new XfChatRequest.Chat());
    }

    /**
     * 获取Payload
     *
     * @param texts
     * @return XfChatRequest.Payload
     */

    private static XfChatRequest.Payload getPayload(List<XfChatRequest.Text> texts) {
        return new XfChatRequest.Payload(
                new XfChatRequest.Message(texts)
        );
    }

    /**
     * 获取Request
     *
     * @param texts
     * @return XfChatRequest
     */
    public static XfChatRequest getRequest(List<XfChatRequest.Text> texts) {
        return XfChatRequest.builder()
                .header(getHeader())
                .parameter(getParameter())
                .payload(getPayload(texts))
                .build();
    }
}
