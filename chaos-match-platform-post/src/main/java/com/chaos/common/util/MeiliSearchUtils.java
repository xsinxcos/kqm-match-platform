package com.chaos.common.util;

import com.alibaba.fastjson.JSON;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: MeiliSearchUtils
 * @author: xsinxcos
 * @create: 2024-02-19 05:16
 **/
@Component
@Slf4j
public class MeiliSearchUtils {

    @Value("${MeiliSearch.adminApiKey}")
    private String apiKey;
    @Value("${MeiliSearch.hostUrl}")
    private String hostUrl;

    private Client client;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchDocumentBo<T> {
        private List<T> data;
        private Integer total;
    }

    public MeiliSearchUtils() {
        initClient();
    }

    @PostConstruct
    private void initClient() {
        if (hostUrl != null && apiKey != null) {
            client = new Client(new Config(hostUrl, apiKey));
        }
    }

    /**
     * 添加新的文档
     *
     * @param source 对象
     * @param uid    索引名称
     */
    public void addDocumentByIndex(Object source, String uid) {
        Index index = client.index(uid);
        index.addDocuments(JSON.toJSONStringWithDateFormat(source, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    }

    /**
     * 添加新的索引
     *
     * @param indexName 索引名称
     */
    public void addIndex(String indexName) {
        client.index(indexName);
    }

    /**
     * 根据索引批量查找文档
     *
     * @param uid           索引
     * @param searchRequest 查询请求
     * @param clazz         类名
     * @return List<V>
     */

    public <V> SearchDocumentBo<V> searchDocumentArrByIndex(String uid, SearchRequest searchRequest, Class<V> clazz) {
        Index index = client.index(uid);

        SearchResult search = (SearchResult) index.search(searchRequest);

        List<V> result = search.getHits().stream()
                .map(o -> JSON.toJSONString(o.getOrDefault("_formatted", o)))
                .map(o -> JSON.parseObject(o, clazz))
                .collect(Collectors.toList());

        return new SearchDocumentBo<>(result, search.getEstimatedTotalHits());
    }

    /**
     * 更新文档
     *
     * @param source 对象
     * @param uid    索引名称
     */

    public void updateDocumentByIndex(Object source, String uid) {
        Index index = client.index(uid);
        index.addDocuments(JSON.toJSONStringWithDateFormat(source, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    }

    /**
     * 删除文档
     *
     * @param uid 索引名称
     * @param id  文档唯一键
     */

    public void deleteDocumentByIndex(String uid, String id) {
        client.index(uid).deleteDocument(id);
    }

    /**
     * 根据索引查找单个文档
     *
     * @param uid   索引
     * @param id    唯一键
     * @param clazz 类名
     * @return V
     */
    public <V> V searchDocumentById(String uid, String id, Class<V> clazz) {
        Index index = client.index(uid);
        return index.getDocument(id, clazz);
    }
}
