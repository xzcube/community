package com.xzcube.community.mapper;

import com.xzcube.community.model.Comment;
import org.apache.ibatis.annotations.*;

/**
 * @author xzcube
 * @date 2021/5/31 15:45
 */
@Mapper
public interface CommentMapper {
    @Insert("insert into comment(parent_id, type, commentator, gmt_create, gmt_modified, content) " +
            "values(#{parentId}, #{type}, #{commentator}, #{gmtCreate}, #{gmtModified}, #{content})")
    void insert(Comment comment);

    @Select("select * from comment where id=#{parentId}")
    Comment findByParentId(@Param("parentId") Integer parentId);
}
