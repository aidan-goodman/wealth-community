package top.aidan.community.dao;

import org.apache.ibatis.annotations.*;
import top.aidan.community.entity.LoginTicket;

/**
 * @author Aidan
 * @createTime 2021/11/15 17:15
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Mapper
public interface LoginTicketMapper {

    /**
     * 对登录成功的用户存储登录凭证，采用注解的方式，id 自动生成
     *
     * @param ticket 当前用户的凭证
     */
    @Insert({
            "INSERT into login_ticket(user_id, ticket, status, expired) ",
            "VALUES (#{userId}, #{ticket}, #{status}, #{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertLoginTicket(LoginTicket ticket);

    /**
     * 登录时查询凭证
     *
     * @param ticket 用户登录时的凭证
     * @return ticket str
     */
    @Select({
            "select id, user_id, ticket, status, expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    /**
     * 修改用户的登录状态
     *
     * @param ticket 本次登录的凭证
     * @param status 本次登录将要变成的状态
     * @return update result
     */
    @Update({
            "<script>",
            "update login_ticket set status=#{status} ",
            "<if test=\"#{ticket}!=null\">",
            "where ticket=#{ticket}",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);

}
