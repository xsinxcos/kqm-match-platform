package com.chaos;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.entity.Hotel;
import com.chaos.entity.HotelDoc;
import com.chaos.service.IHotelService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class HotelDemoApplicationTests {

    private RestHighLevelClient client;
    @Autowired
    IHotelService iHotelService;



    @Test
    public void test(){
        WxMaService wxMaService = new WxMaServiceImpl();
        String appid = "wxaf871f55adccb5c5";
        String code = "0c3ZM30w3Vzk323EHF1w3NhCS60ZM305";
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            System.out.println(session.getOpenid());
        } catch (WxErrorException e) {
//            log.error(e.getMessage(), e);
//            return e.toString();
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
        System.out.println("111");
    }
    @Test
    public void testAddDoc() throws IOException {
        BulkRequest request = new BulkRequest();
        LambdaQueryWrapper<Hotel> queryWrapper = new LambdaQueryWrapper<>();
        List<Hotel> list = iHotelService.list(queryWrapper);
        for (Hotel hotel : list) {
            HotelDoc hotelDoc = new HotelDoc(hotel);
            request.add(new IndexRequest("hotel").type("test").id(hotelDoc.getId().toString()).
                    source(JSON.toJSONString(hotelDoc), XContentType.JSON));
        }
//        IndexRequest indexRequest = new IndexRequest("hotel");

//        indexRequest.source(JSON.toJSONString(new HotelDoc(iHotelService.getById(36934L))),XContentType.JSON);
//        client.delete(new DeleteRequest("hotel").type("test").id("36934"), RequestOptions.DEFAULT);
        client.bulk(request, RequestOptions.DEFAULT);
    }

    @Test
    void getDoc() throws IOException {
        GetRequest request = new GetRequest("hotel", "test", "416260");
        GetResponse documentFields = client.get(request, RequestOptions.DEFAULT);
        String sourceAsString = documentFields.getSourceAsString();

        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);

        System.out.println(hotelDoc.toString());
    }

    @Test
    void updateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest("hotel", "test", "416260");
        request.doc(
                "name", "惠来浦东东站华美达酒店"
        );
        client.update(request, RequestOptions.DEFAULT);
    }

    @Test
    void matchAll() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source()
                .query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("name", "北京"))
                        .filter(QueryBuilders.rangeQuery("price").gt(1000)
                ));

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        long totalHits = response.getHits().totalHits;
        int pageSize = 10;
        int pageNum = (int) (totalHits / pageSize) + 1;
        for (int i = 0; i < pageNum; i++) {
            searchRequest.source().from(i).size(10);
            SearchHit[] hits = client.search(searchRequest, RequestOptions.DEFAULT).getHits().getHits();
            for (SearchHit hit : hits) {
                System.out.println(JSON.parseObject(hit.getSourceAsString(), HotelDoc.class).toString());
            }
        }


    }

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://127.0.0.1:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }
}
