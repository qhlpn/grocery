package com.nowcoder.dao;

import com.nowcoder.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title,content,comment_count,created_date,user_id ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into" + TABLE_NAME + "(" + INSERT_FIELDS +
            ") value (#{title},#{content},#{commentCount},#{createdDate},#{userId})"})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Select({"select" + SELECT_FIELDS + "from" + TABLE_NAME + "where id=#{id}"})
    Question selectById(int id);

    @Update({"update" + TABLE_NAME + "set comment_count = comment_count + 1 where id = #{id}"})
    void updateQuestionCommentCount(int id);
}
