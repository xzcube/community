package com.xzcube.community.mapper;

import com.xzcube.community.model.Comment;
import com.xzcube.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xzcube
 * @date 2021/5/31 15:45
 */
@Mapper
public interface CommentMapper {
    @Insert("insert into comment(parent_id, type, commentator, gmt_create, gmt_modified, content) " +
            "values(#{parentId}, #{type}, #{commentator}, #{gmtCreate}, #{gmtModified}, #{content})")
    void insert(Comment comment);

    /**
     * 通过二级评论的parentId寻找到相应的一级评论
     * @param parentId
     * @return
     */
    @Select("select * from comment where id=#{parentId}")
    Comment findByParentId(@Param("parentId") Integer parentId);

    /**
     * question的id就是底下评论的parentId，可以根据当前question的id查询到它的一级评论
     * @return
     */
    @Select("select * from comment where parent_id=#{parentId} order by like_count desc")
    List<Comment> findListByParentId(@Param("parentId") Integer parentId);

    @Select("select count(1) from comment where parent_id=#{parentId}")
    Integer commentCount(@Param("parentId") Integer parentId);

    @Select("select * from comment where parent_id = #{parentId} order by GMT_CREATE desc limit #{offset}, 4")
    List<Comment> findByQuestionId(@Param("parentId") Integer parentId, @Param(value = "offset") Integer offset);
}
