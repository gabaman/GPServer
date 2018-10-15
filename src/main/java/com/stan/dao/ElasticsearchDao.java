package com.stan.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stan.model.ESResult;
import com.stan.model.pojo.GPItem;
import com.stan.model.pojo.GPWalkthrough;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public class ElasticsearchDao {

    @Autowired
    private TransportClient transportClient;
    private static final ObjectMapper mapper = new ObjectMapper();

    private final static String gameContentIndex="game";
    private final static String gameContentType="content";

    public Boolean saveWalkthrough(GPWalkthrough walkthrough,String image) {

        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();

        Map tmp = walkthrough.toMap(image);
        tmp.put("isItem",1);
        tmp.put("image", image);

        IndexRequest indexRequest = transportClient.prepareIndex(gameContentIndex,gameContentType,walkthrough.getTypeid().toString()).setSource(tmp).request();
        bulkRequestBuilder.add(indexRequest);
        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            System.out.println("批量创建索引错误！");
            return false;
        }
        System.out.println("批量创建索引成功");
        return true;

    }

    public Boolean saveItem(GPItem item) {

        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();

        Map tmp = item.toMap();
        tmp.put("isItem",0);
        IndexRequest   indexRequest = transportClient.prepareIndex(gameContentIndex,gameContentType,item.getTypeid().toString()).setSource(tmp).request();

        bulkRequestBuilder.add(indexRequest);
        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            System.out.println("批量创建索引错误！");
            return false;
        }
        System.out.println("批量创建索引成功");
        return true;

    }

    public ESResult searchAll(String name, Integer
            start, Integer size) {
        long startTime = System.currentTimeMillis();
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        QueryBuilder multiMatch = QueryBuilders.multiMatchQuery(name, "title", "content").operator(Operator.AND);
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .preTags("<span style=\"color:red\">")
                .postTags("</span>")
                .field("title")
                .field("content");

        SearchResponse response = transportClient.prepareSearch(gameContentIndex)
                .setTypes(gameContentType)
                .setQuery(multiMatch)
                .highlighter(highlightBuilder)
                .setFrom(start * size)
                .setSize(size)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();

        if (hits.getTotalHits() > 0) {
            System.out.println("共搜索到" + hits.getTotalHits() + "条结果!");
            for (SearchHit hit : hits) {

                Map<String, Object> map = hit.sourceAsMap();

                HighlightField hTitle = hit.getHighlightFields().get("title");
                HighlightField hContent = hit.getHighlightFields().get("content");
                if (hTitle != null) {
                    String highlightTitle = "";
                    Text[] fragments = hTitle.fragments();
                    for (Text text : fragments) {
                        highlightTitle += text;
                    }
                    map.put("title", highlightTitle);
                }
                if (hContent != null) {
                    String highlightContent = "";
                    Text[] fragments = hContent.fragments();
                    for (Text text : fragments) {
                        highlightContent += text;
                    }
                    map.put("content", highlightContent);
                }
                list.add(map);

            }
        } else {
            System.out.println("搜到0条结果");
        }
        System.out.println("newslist" + list);
        System.out.println(list.size());
        long endTime = System.currentTimeMillis();

        ESResult result = new ESResult();
        result.list = list;
        result.totalHits = String.valueOf(hits.getTotalHits());
        result.totalTime = String.valueOf((endTime - startTime));

        return result;

    }



}
