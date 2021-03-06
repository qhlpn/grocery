package com.example.elasticsearch;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class ElasticsearchApplicationTests {

    @Autowired
    private EsUtil esUtil;

    @Test
    void contextLoads() {
    }

    @Test
    void case1() {

//--
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//        MultiMatchQueryBuilder scoreQuery = QueryBuilders.multiMatchQuery(
//                "系统学习ElasticSearch", "title", "content", "tag");
//        searchSourceBuilder.query(scoreQuery);
//
//        MultiMatchQueryBuilder reScoreQuery = QueryBuilders.multiMatchQuery(
//                "系统学习ElasticSearch", "title", "content", "tag")
//                .type(MultiMatchQueryBuilder.Type.PHRASE);
//        QueryRescorerBuilder queryRescorerBuilder = new QueryRescorerBuilder(reScoreQuery);
//        searchSourceBuilder.addRescorer(queryRescorerBuilder);
//        searchSourceBuilder.size(10).from(0);
//
//        EsUtil.PageResponse<Blog> pageResponse = esUtil.search("demo1_blog", searchSourceBuilder, Blog.class, 10, 0);

//--
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
//        boolQuery.should(QueryBuilders.matchQuery("tag", "系统学习ElasticSearch").boost(3))
//                .should(QueryBuilders.matchQuery("title", "系统学习ElasticSearch").boost(2))
//                .should(QueryBuilders.matchQuery("content", "系统学习ElasticSearch").boost(1));
//
//        searchSourceBuilder.query(boolQuery);
//        searchSourceBuilder.size(10).from(0);
//
//        EsUtil.PageResponse<Blog> pageResponse = esUtil.search("demo1_blog", searchSourceBuilder, Blog.class, 10, 0);

//--
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
//        boolQuery.should(QueryBuilders.matchQuery("tag", "系统学习ElasticSearch").boost(3))
//                .should(QueryBuilders.matchQuery("title", "系统学习ElasticSearch").boost(2))
//                .should(QueryBuilders.matchQuery("content", "系统学习ElasticSearch").boost(1))
//                .filter(QueryBuilders.termQuery("author", "方才兄"))
//                .filter(QueryBuilders.termsQuery("tag.keyword", "ElasticSearch", "倒排序索引"))
//                .filter(QueryBuilders.rangeQuery("influence").gte(5).lte(15));
//
//        searchSourceBuilder.query(boolQuery);
//        searchSourceBuilder.size(10).from(0);
//
//        EsUtil.PageResponse<Blog> pageResponse = esUtil.search("demo1_blog", searchSourceBuilder, Blog.class, 10, 0);

//--
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
//                .filter(QueryBuilders.multiMatchQuery("系统学习ElasticSearch", "title", "content", "tag"))
//                .filter(QueryBuilders.termQuery("author", "方才兄"))
//                .filter(QueryBuilders.termsQuery("tag.keyword", "ElasticSearch", "倒排序索引"))
//                .filter(QueryBuilders.rangeQuery("influence").gte(10).lte(15));
//
//        searchSourceBuilder.query(boolQuery);
//        searchSourceBuilder.sort("view", SortOrder.DESC);
//
//        EsUtil.PageResponse<Blog> pageResponse = esUtil.search("demo1_blog", searchSourceBuilder, Blog.class, 10, 0);


//--
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        String query = "关zhuwo";
        Pattern pattern = Pattern.compile("[^\u4E00-\u9FA5]");
        Matcher matcher = pattern.matcher(query);
        String chinese = matcher.replaceAll("");
        System.out.println(chinese);

        // 必须包含中文
        if (StringUtils.isNotBlank(chinese))
            boolQuery.filter(QueryBuilders.matchQuery("nickName", chinese).analyzer("standard"));

        // 中文 + 全拼 + 首字母 混合检索
        boolQuery.must(QueryBuilders.matchPhraseQuery("nickName.pinyin", query));
        // 高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("nickName.pinyin");

        searchSourceBuilder.query(boolQuery).highlighter(highlightBuilder).size(10).from(0);

        EsUtil.PageResponse<User> response = esUtil.search("user_index", searchSourceBuilder, User.class, 0, 10);

        System.out.println("------");
    }

}