package top.aidan.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.aidan.community.entity.User;

/**
 * Created by Aidan on 2021/10/13 14:41
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 *
 * @author Aidan
 */

@Mapper
public interface UserMapper {

    /**
     * 通过 id 查询相应用户
     *
     * @param id userId
     * @return User 对象
     */
    User selectById(
            @Param("id") int id);

    /**
     * 根据 username 查询相应用户
     *
     * @param username 用户名
     * @return User 对象
     */
    User selectByName(
            @Param("username") String username);

    /**
     * 根据 Email 查询对象
     *
     * @param email 邮箱
     * @return User 对象
     */
    User selectByEmail(
            @Param("email") String email);

    /**
     * 插入用户
     *
     * @param user 封装好的用户对象
     * @return result
     */
    int insertUser(User user);

    /**
     * 更新用户状态
     *
     * @param id     userId
     * @param status 用户状态
     * @return result
     */
    int updateStatus(
            @Param("id") int id,
            @Param("status") int status);

    /**
     * 更新头像
     *
     * @param id        userId
     * @param headerUrl 头像链接
     * @return result
     */
    int updateHeader(
            @Param("id") int id,
            @Param("headerUrl") String headerUrl);

    /**
     * 更改密码
     *
     * @param id       userId
     * @param password 密码
     * @return result
     */
    int updatePassword(
            @Param("id") int id,
            @Param("password") String password);
}
