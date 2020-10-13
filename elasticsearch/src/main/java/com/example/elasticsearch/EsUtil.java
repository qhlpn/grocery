package com.example.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class EsUtil {

    @Autowired
    private EsConfig esConfig;

    private static int retryLimit = 3;


    /**
     * 搜索
     *
     * @param index
     * @param searchSourceBuilder
     * @param clazz  需要封装的obj
     * @param pageNum
     * @param pageSize
     * @return PageResponse<T>
     */
    public <T> PageResponse<T> search(String index, SearchSourceBuilder searchSourceBuilder, Class<T> clazz,
                                      Integer pageNum, Integer pageSize) {

        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        log.info("DSL语句为：{}", searchRequest.source().toString());
        try {
            assert esConfig.getObject() != null;
            SearchResponse response = esConfig.getObject().search(searchRequest, RequestOptions.DEFAULT);
            PageResponse<T> pageResponse = new PageResponse<>();
            pageResponse.setPageNum(pageNum);
            pageResponse.setPageSize(pageSize);
            pageResponse.setTotal(response.getHits().getTotalHits());
            List<T> dataList = new ArrayList<>();
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                dataList.add(JSONObject.parseObject(hit.getSourceAsString(), clazz));
            }
            pageResponse.setData(dataList);
            return pageResponse;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 聚合
     *
     * @param index
     * @param searchSourceBuilder
     * @param aggName 聚合名
     * @return Map<Integer, Long>  key:aggName   value: doc_count
     */
    public Map<Integer, Long> aggSearch(String index, SearchSourceBuilder searchSourceBuilder, String aggName) {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        log.info("DSL语句为：{}", searchRequest.source().toString());
        try {
            assert esConfig.getObject() != null;
            SearchResponse response = esConfig.getObject().search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = response.getAggregations();
            Terms terms = aggregations.get(aggName);
            List<? extends Terms.Bucket> buckets = terms.getBuckets();
            Map<Integer, Long> responseMap = new HashMap<>(buckets.size());
            buckets.forEach(bucket -> responseMap.put(bucket.getKeyAsNumber().intValue(), bucket.getDocCount()));
            return responseMap;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;

    }


    /**
     * 新增或者更新文档
     * <p>
     * 对于更新文档，建议可以直接使用新增文档的API，替代 UpdateRequest
     * 避免因对应id的doc不存在而抛异常：document_missing_exception
     *
     * @param obj
     * @param index
     * @return
     */
    public Boolean addOrUptDocToEs(Object obj, String index) {

        try {
            IndexRequest indexRequest = new IndexRequest(index).id(getESId(obj))
                    .source(JSON.toJSONString(obj), XContentType.JSON);
            int times = 0;
            while (times < retryLimit) {
                assert esConfig.getObject() != null;
                IndexResponse indexResponse = esConfig.getObject().index(indexRequest, RequestOptions.DEFAULT);

                if (indexResponse.status().equals(RestStatus.CREATED) || indexResponse.status().equals(RestStatus.OK)) {
                    return true;
                } else {
                    log.info(JSON.toJSONString(indexResponse));
                    times++;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("Object = {}, index = {}, id = {} , exception = {}", obj, index, getESId(obj), e.getMessage());
        }
        return null;

    }


    /**
     * 删除文档
     *
     * @param index
     * @param id
     * @return
     */
    public Boolean deleteDocToEs(Integer id, String index) {
        try {
            DeleteRequest request = new DeleteRequest(index, "_doc", id.toString());

            int times = 0;
            while (times < retryLimit) {
                assert esConfig.getObject() != null;
                DeleteResponse delete = esConfig.getObject().delete(request, RequestOptions.DEFAULT);

                if (delete.status().equals(RestStatus.OK)) {
                    return true;
                } else {
                    log.info(JSON.toJSONString(delete));
                    times++;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("index = {}, id = {} , exception = {}", index, id, e.getMessage());
        }
        return null;
    }


    /**
     * 批量插入 或者 更新
     *
     * @param array 数据集合
     * @param index
     * @return
     */
    public Boolean batchAddOrUptToEs(JSONArray array, String index) {

        try {
            BulkRequest request = new BulkRequest();
            for (Object obj : array) {
                IndexRequest indexRequest = new IndexRequest(index).id(getESId(obj))
                        .source(JSON.toJSONString(obj), XContentType.JSON);
                request.add(indexRequest);
            }
            assert esConfig.getObject() != null;
            BulkResponse bulk = esConfig.getObject().bulk(request, RequestOptions.DEFAULT);

            return bulk.status().equals(RestStatus.OK);
        } catch (Exception e) {
            log.error("index = {}, exception = {}", index, e.getMessage());
        }
        return null;
    }


    /**
     * 批量删除
     *
     * @param deleteIds 待删除的 _id list
     * @param index
     * @return
     */
    public Boolean batchDeleteToEs(List<Integer> deleteIds, String index) {
        try {
            BulkRequest request = new BulkRequest();
            for (Integer deleteId : deleteIds) {
                DeleteRequest deleteRequest = new DeleteRequest(index, "_doc", deleteId.toString());
                request.add(deleteRequest);
            }
            assert esConfig.getObject() != null;
            BulkResponse bulk = esConfig.getObject().bulk(request, RequestOptions.DEFAULT);
            return bulk.status().equals(RestStatus.OK);
        } catch (Exception e) {
            log.error("index = {}, exception = {}", index, e.getMessage());
        }
        return null;
    }


    /**
     * 将obj的id 作为 doc的_id
     *
     * @param obj
     * @return
     */
    private String getESId(Object obj) {
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
        Object id = jsonObject.get("id");
        return JSON.toJSONString(id);
    }


    @Data
    public class PageResponse<T> {

        private int pageNum;
        private int pageSize;
        private long total;
        private List<T> data;

    }

}


