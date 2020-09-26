package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface LoginTicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id,expired,status,ticket ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into" + TABLE_NAME + "(" + INSERT_FIELDS +
            ") value (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket loginTicket);

    @Select({"select" + SELECT_FIELDS + "from" + TABLE_NAME + "where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update" + TABLE_NAME + "set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("status") int status, @Param("ticket") String ticket);

}
