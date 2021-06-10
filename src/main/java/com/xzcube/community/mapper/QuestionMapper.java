package com.xzcube.community.mapper;

import com.xzcube.community.model.Question;
import org.apache.ibatis.annotations.*;

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
    void create(Question question);

    @Select("select * from question order by GMT_CREATE desc limit #{offset}, #{size}")
    List<Question> findAllQuestions(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    /**
     * 查询话题总数
     * @return
     */
    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{creator} order by GMT_CREATE desc limit #{offset}, #{size}")
    List<Question> findByCreator(@Param("creator") Integer creator, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator = #{creator}")
    Integer countByCreator(@Param("creator") Integer creator);

    @Select("select * from question where id = #{id}")
    Question findById(@Param("id") Integer id);

    @Update("update question set title=#{title},  description=#{description}, gmt_modified=#{gmtModified}, tag=#{tag} where id=#{id}")
    void update(Question question);

    @Update("update question set view_count=view_count+1 where id=#{id}")
    void incView(@Param("id") Integer id);

    @Update("update question set comment_count=comment_count+1 where id=#{id}")
    void incComment(@Param("id") Integer id);

    @Select("select id, title from question ORDER BY view_count DESC LIMIT #{offset}, #{count}")
    List<Question> findHotQuestions(@Param(value = "offset") Integer offset, @Param(value = "count") Integer count);

    /**
     * 寻找相关话题
     * @param id
     * @param regexpTag
     * @return
     */
    @Select("select * from question where id != #{id} and tag regexp #{regexpTag} order by gmt_create desc limit 10")
    List<Question> selectRelated (@Param("id") Integer id, @Param("regexpTag") String regexpTag);

    @Update("update question set comment_count=comment_count-1 where id=#{questionId}")
    void descCommentCount(@Param("questionId") Integer questionId);

    @Delete("delete from question where id=#{questionId}")
    void delById(@Param("questionId") Integer questionId);
}
