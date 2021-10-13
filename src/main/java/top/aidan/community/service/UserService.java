package top.aidan.community.service;

import org.springframework.stereotype.Service;
import top.aidan.community.dao.UserMapper;
import top.aidan.community.entity.User;

/**
 * Created by Aidan on 2021/10/13 9:05
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
