package com.xzcube.community.mapper;

import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author xzcube
 * @date 2021/5/26 19:28
 * 将发布的问题添加到数据库
 */
@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title, description, gmt_create, gmt_modified, creator, tag) values" +
            "(#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    public void create(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    List<Question> findAllQuestions(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    /**
     * 查询话题总数
     * @return
     */
    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{creator} limit #{offset}, #{size}")
    List<Question> findByCreator(@Param("creator") Integer creator, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator = #{creator}")
    Integer countByCreator(@Param("creator") Integer creator);
}
