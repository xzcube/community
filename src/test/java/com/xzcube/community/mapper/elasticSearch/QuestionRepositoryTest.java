package com.xzcube.community.mapper.elasticSearch;

import com.xzcube.community.mapper.QuestionMapper;
import com.xzcube.community.model.Question;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

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
        Question question = questionMapper.findById(111);
        question.setDescription("猫猫好可爱，但是会咬人，咬人就要打针");
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
    public void testSearch(){
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "description"))
                .withSort(SortBuilders.fieldSort("commentCount").order(SortOrder.DESC))  // 根据commentCount降序排列
                .withSort(SortBuilders.fieldSort("viewCount").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("gmtCreate").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("<em>")
                ).build();  // 设置高亮显示
    }
}