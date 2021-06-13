package com.xzcube.community.mapper;

import com.xzcube.community.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

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

    @Select("select * from comment where parent_id = #{parentId} and type=1 order by GMT_CREATE desc limit #{offset}, 4")
    List<Comment> findByQuestionId(@Param("parentId") Integer parentId, @Param(value = "offset") Integer offset);

    @Select("select * from comment where parent_id = #{parentId} and type=#{type} order by GMT_CREATE desc")
    List<Comment> findByCommentId(@Param("parentId") Integer parentId, @Param("type") Integer type);

    /**
     * 发布二级评论后让对应一级评论的评论数+1
     * @param parentId
     */
    @Update("update comment set comment_count=comment_count+1 where id=#{parentId}")
    void incCommentId(@Param("parentId") Integer parentId);

    @Delete("delete from comment where id=#{commentId} and type=1")
    void delCommentById(@Param("commentId") Integer commentId);

    /**
     * 根据parentId删除二级评论
     * @param commentId
     */
    @Delete("delete from comment where parent_id=#{commentId} and type=2")
    void delCommentByParentId(@Param("commentId") Integer commentId);

    /**
     * 根据parentId删除一级评论
     * @param parentId
     */
    @Delete("delete from comment where parent_id=#{parentId} and type=1")
    void delFirstCommentByParentId(@Param("parentId") Integer parentId);

    @Select("select id from comment where parent_id=#{parentId}")
    List<Integer> selectByParentId(@Param("parentId") Integer parentId);
}
