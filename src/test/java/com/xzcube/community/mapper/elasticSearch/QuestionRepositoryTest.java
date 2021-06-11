package com.xzcube.community.mapper.elasticSearch;

import com.xzcube.community.mapper.QuestionMapper;
import com.xzcube.community.model.Question;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author xzcube
 * @date 2021/6/10 10:55
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class QuestionRepositoryTest {
    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionRepository questionRepository;

    @Qualifier("myEsClient")
    @Autowired
    RestHighLevelClient esClient;

    @Test
    public void testInsert(){
        questionRepository.save(questionMapper.findById(111));
        questionRepository.save(questionMapper.findById(112));
        questionRepository.save(questionMapper.findById(113));
    }

    @Test
    public void testInsertList(){
        questionRepository.saveAll(questionMapper.findAllQuestions(0, 20));
    }

    /**
     * 修改就是查询到相应数据之后，手动修改数据，再次保存
     */
    @Test
    public void testUpdate(){
        Question question = questionMapper.findById(112);
        question.setDescription("猫猫好可爱，我要撸猫");
        questionRepository.save(question);
    }

    @Test
    public void testDelete(){
        questionRepository.deleteById(111);
    }

    @Test
    public void testDeleteAll(){
        questionRepository.deleteAll();
    }

    @Test
    public void testSearch() throws IOException {
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.multiMatchQuery("一般", "title", "description"));
        // 设置查询条件
        builder.sort(SortBuilders.fieldSort("commentCount").order(SortOrder.DESC))  // 根据commentCount降序排列
                .sort(SortBuilders.fieldSort("viewCount").order(SortOrder.DESC))  // 根据viewCount降序排列
                .sort(SortBuilders.fieldSort("gmtCreate").order(SortOrder.DESC)); // 根据gmtCreate降序排列
        builder.from(0);
        builder.size(10);
        request.source(builder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        /*NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery("好奇", "title", "description"))
                .withSort(SortBuilders.fieldSort("commentCount").order(SortOrder.DESC))  // 根据commentCount降序排列
                .withSort(SortBuilders.fieldSort("viewCount").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("gmtCreate").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("<em>")
                ).build();  // 设置高亮显示*/
        //Page<Question> page = questionRepository.search(nativeSearchQuery);
        //System.out.println(page.getTotalElements());
    }

    /**
     * 高亮查询
     */
    @Test
    public void highLightTest() throws IOException {
        SearchRequest request = new SearchRequest();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "好奇");
        SearchSourceBuilder builder = new SearchSourceBuilder();

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("description");
        builder.highlighter(highlightBuilder);
        builder.query(termQueryBuilder);
        
        request.source(builder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

    }
}