package com.xzcube.community.service.impl;

//import com.xzcube.community.mapper.elasticSearch.QuestionRepository;

import com.xzcube.community.mapper.elasticSearch.QuestionRepository;
import com.xzcube.community.model.Question;
import com.xzcube.community.service.ElasticsearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author xzcube
 * @date 2021/6/11 9:48
 * 搜索功能应该整合elasticsearch进行实现，但是现在
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Qualifier("myEsClient")
    @Autowired
    private RestHighLevelClient esClient;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * 将帖子存在elasticsearch中
     * @param question
     */
    public void saveQuestion(Question question){
        questionRepository.save(question);
    }

    /**
     * 将帖子从elasticsearch中删除
     * @param id
     */
    public void deleteQuestion(Integer id){
        questionRepository.deleteById(id);
    }

    /**
     * 搜索帖子
     * @param keyWord 搜索的关键字
     * @param current  当前显示的是第几页
     * @param limit 每页显示条数
     * @return
     */

    public SearchHits searchQuestion(String keyWord, int current, int limit) throws IOException {
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.multiMatchQuery(keyWord, "title", "description"));
        // 设置查询条件
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("description");
        highlightBuilder.field("title");

        builder.highlighter(highlightBuilder);

        builder.sort(SortBuilders.fieldSort("commentCount").order(SortOrder.DESC))  // 根据commentCount降序排列
                .sort(SortBuilders.fieldSort("viewCount").order(SortOrder.DESC))  // 根据viewCount降序排列
                .sort(SortBuilders.fieldSort("gmtCreate").order(SortOrder.DESC)); // 根据gmtCreate降序排列

        builder.from((current - 1) * limit);
        builder.size(limit);
        request.source(builder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        return hits;
    }
}
