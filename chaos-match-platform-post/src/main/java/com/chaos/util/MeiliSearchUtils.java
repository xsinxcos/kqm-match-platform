package com.chaos.util;

import com.alibaba.fastjson.JSON;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description: MeiliSearchUtils
 * @author: xsinxcos
 * @create: 2024-02-19 05:16
 **/
@Component
public class MeiliSearchUtils {
    private static String apiKey;
    private static String hostUrl;

    private static Client client;

    public MeiliSearchUtils() {
        client = new Client(new Config(hostUrl, apiKey));
    }

    @Value("${MeiliSearch.hostUrl}")
    public void setHostUrl(String hostUrl) {
        MeiliSearchUtils.hostUrl = hostUrl;
    }

    @Value("${MeiliSearch.adminApiKey}")
    public void setApiKey(String apiKey) {
        MeiliSearchUtils.apiKey = apiKey;
    }

    /**
     * 添加新的文档
     * @param source 对象
     * @param uid   索引名称
     */
    public static void addDocumentByIndex(Object source, String uid) {
        Index index = client.index(uid);
        index.addDocuments(JSON.toJSONString(source));
    }

    /**
     * 添加新的索引
     * @param indexName 索引名称
     */
    public static void addIndex(String indexName) {
        client.index(indexName);
    }

    /**
     * 根据索引查找文档
     * @param uid 索引
     * @param searchRequest 查询请求
     * @param clazz 类名
     * @return List<V>
     */

    public static <V> List<V> searchDocumentByIndex(String uid , SearchRequest searchRequest , Class<V> clazz) {
        Index index = client.index(uid);

        ArrayList<HashMap<String, Object>> hits = index.search(searchRequest).getHits();
        String jsonString = JSON.toJSONString(hits);
        List<V> result = JSON.parseArray(jsonString, clazz);

        return result;
    }
}
