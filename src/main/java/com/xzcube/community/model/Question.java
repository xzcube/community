package com.xzcube.community.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * @author xzcube
 * @date 2021/5/26 19:34
 *
 * 封装发布问题的类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "question") // 表明这是需要用到elasticsearch的的类,索引名为question
public class Question {
    @Id
    private Integer id;

    // type:属性类型
    // analyzer:存储时的解析器 ik_max_word:将标题按照最多词汇拆分(增加搜索范围)
    // searchAnalyzer:搜索时候的解析器 ik_smart按照需要进行拆分
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    // title和description是需要被搜索的字段，一般就会这么配置
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String description;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String tag;
    @Field(type = FieldType.Date)
    private Long gmtCreate;
    @Field(type = FieldType.Date)
    private Long gmtModified;
    @Field(type = FieldType.Integer)
    private Integer creator;
    @Field(type = FieldType.Integer)
    private Integer viewCount;
    @Field(type = FieldType.Integer)
    private Integer commentCount;
    @Field(type = FieldType.Integer)
    private Integer likeCount;
}
