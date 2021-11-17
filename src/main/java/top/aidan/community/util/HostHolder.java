package top.aidan.community.util;

import org.springframework.stereotype.Component;
import top.aidan.community.entity.User;

/**
 * @author Aidan
 * @createTime 2021/11/17 19:57
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 * <p>
 * 负责在多线程的场景下持有用户信息，代替 Session 对象
 */

@Component
public class HostHolder {

    private final ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
