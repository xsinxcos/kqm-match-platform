package com.meilisearch.sdk;

import com.alibaba.fastjson.JSON;
import com.meilisearch.sdk.model.MatchingStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Search request query string builder
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
public class SearchRequest {
    private String q;
    private Integer offset;
    private Integer limit;
    private String[] attributesToRetrieve;
    private String[] attributesToCrop;
    private Integer cropLength;
    private String cropMarker;
    private String highlightPreTag;
    private String highlightPostTag;
    private MatchingStrategy matchingStrategy;
    private String[] attributesToHighlight;
    private String[] attributesToSearchOn;
    private String[] filter;
    private String[][] filterArray;
    private Boolean showMatchesPosition;
    private String[] facets;
    private String[] sort;
    protected Integer page;
    protected Integer hitsPerPage;

    /**
     * Constructor for SearchRequest for building search queries with the default values: offset: 0,
     * limit: 20, attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200,
     * attributesToHighlight: null, filter: null, showMatchesPosition: false, facets: null, sort:
     * null
     *
     * @param q Query String
     */
    public SearchRequest(String q) {
        this();
        this.q = q;
    }

    /**
     * Method to set the Query String
     *
     * <p>This method is an alias of setQ()
     *
     * @param q Query String
     * @return SearchRequest
     */
    public SearchRequest setQuery(String q) {
        return setQ(q);
    }

    /**
     * Method that returns the JSON String of the SearchRequest
     *
     * @return JSON String of the SearchRequest query
     */
    @Override
    public String toString() {

        if (Objects.nonNull(this.filterArray)) {
            this.filter = Stream.of(this.filterArray)
                    .flatMap(Stream::of)
                    .toArray(String[]::new);

            this.filterArray = null;
        }

        return JSON.toJSONString(this);
    }
}
