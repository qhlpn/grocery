package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id,to_id,content,created_date,has_read,conversation_id ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into" + TABLE_NAME + "(" + INSERT_FIELDS +
            ") value (#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select" + SELECT_FIELDS + "from" + TABLE_NAME + "where conversation_id=#{conversationId} " +
            "order by created_date desc limit #{offset}, #{limit}"})
    List<Message> selectByConversationId(@Param("conversationId") String conversationId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    // SELECT * FROM (SELECT * FROM message ORDER BY created_date DESC LIMIT 99999999999) tt GROUP BY conversation_id ORDER BY created_date DESC LIMIT 0,2;
    @Select({"select any_value(from_id) as from_id" +
            ",any_value(to_id) as to_id" +
            ",any_value(content) as content" +
            ",any_value(created_date) as created_date" +
            ",any_value(has_read) as has_read" +
            ",any_value(conversation_id) as conversation_id" +
            ",count(id) as id from ( select * from message where from_id=#{userId} or to_id=#{userId} order by created_date desc limit 99999999) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({"select count(id) from" + TABLE_NAME + "where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Update({"update" + TABLE_NAME + "set has_read = #{hasRead} where conversation_id = #{conversationId} and to_id=#{toId}"})
    void updateReadStatus(@Param("conversationId") String conversationId, @Param("hasRead") int hasRead, @Param("toId") int toId);


//
//    @Delete({"delete from" + TABLE_NAME + "where id=#{id}"})
//    void deleteById(int id);
}
