package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id,created_date,entity_id,entity_type,content,status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into" + TABLE_NAME + "(" + INSERT_FIELDS +
            ") value (#{userId},#{createdDate},#{entityId},#{entityType},#{content},#{status})"})
    int addComment(Comment comment);

    @Select({"select" + SELECT_FIELDS + "from" + TABLE_NAME + "where entity_type=#{entityType} and entity_id=#{entityId} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityType") int entityType,
                                        @Param("entityId") int entityId);

    @Select({"select" + SELECT_FIELDS + "from" + TABLE_NAME + "where id=#{id}"})
    Comment selectCommentById(int id);

    @Select({"select count(id) from" + TABLE_NAME + "where entity_type=#{entityType} and entity_id=#{entityId}"})
    int getCommentCount(@Param("entityType") int entityType,
                        @Param("entityId") int entityId);

    @Select({"select count(id) from comment where user_id=#{userId}"})
    int getUserCommentCount(int userId);

    @Update({"update" + TABLE_NAME + "set status = #{status} where id = #{id}"})
    int updateStatus(int id, int status);

}
