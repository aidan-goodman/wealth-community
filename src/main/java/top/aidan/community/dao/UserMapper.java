package top.aidan.community.dao;

import top.aidan.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    // 加上 @Param 防止出现对应问题
    User selectById(@Param("id") int id);

    User selectByName(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    int insertUser(User user);

    int updateStatus(@Param("id") int id, @Param("status") int status);

    int updateHeader(@Param("id") int id, @Param("headerUrl") String headerUrl);

    int updatePassword(@Param("id") int id, @Param("password") String password);

}
