package top.aidan.community.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.aidan.community.dao.LoginTicketMapper;
import top.aidan.community.dao.UserMapper;
import top.aidan.community.entity.LoginTicket;
import top.aidan.community.entity.User;
import top.aidan.community.util.CommunityConstant;
import top.aidan.community.util.CommunityUtil;
import top.aidan.community.util.MailClient;

import java.util.*;

/**
 * @author Aidan
 * @createTime 2020/10/13 9:05
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Service
public class UserService implements CommunityConstant {
    private final UserMapper userMapper;
    private final MailClient mailClient;
    private final TemplateEngine templateEngine;
    private final LoginTicketMapper loginTicketMapper;

    /**
     * 注入项目的域名和端口号
     */
    @Value("${community.path.domain}")
    private String domain;

    /**
     * 注入项目根路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    public UserService(UserMapper userMapper, MailClient mailClient,
                       TemplateEngine templateEngine, LoginTicketMapper loginTicketMapper) {
        this.userMapper = userMapper;
        this.mailClient = mailClient;
        this.templateEngine = templateEngine;
        this.loginTicketMapper = loginTicketMapper;
    }

    /**
     * 根据 ID 查询用户
     *
     * @param id 用户 ID
     * @return userObj
     */
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    /**
     * 注册用户的方法：格式检查；字段填充；持久化存储
     *
     * @param user 根据用户提交封装的对象
     * @return 错误信息：格式错误；用户以及存在
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>(1);

        // 对注册信息进行格式判断
        if (null == user) {
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        // 判断用户唯一性
        User tempUser = userMapper.selectByName(user.getUsername());
        if (tempUser != null) {
            map.put("usernameMsg", "该账号已经存在");
            return map;
        }

        tempUser = userMapper.selectByEmail(user.getEmail());
        if (tempUser != null) {
            map.put("emailMsg", "该邮箱已经被使用");
            return map;
        }

        // 无误进行存储
        user.setSalt(CommunityUtil.generateUuid().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        // 生成激活码
        user.setActivationCode(CommunityUtil.generateUuid());
        user.setCreateTime(new Date());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userMapper.insertUser(user);

        // 发送验证邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());

        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);

        String content = templateEngine.process("mail/activation", context);

        mailClient.sendMail(user.getEmail(), "账号激活", content);

        return map;
    }

    /**
     * 用户激活方法
     *
     * @param userId 传入需要激活用户的 ID
     * @param code   与用户匹配的激活码
     * @return 返回状态信息
     */
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(user.getId(), 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 注册方法的业务逻辑
     *
     * @param username       用户输入的登录账号
     * @param password       用户密码，需要加密
     * @param expiredSeconds 过期时间
     * @return 返回状态信息
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds) {

        Map<String, Object> map = new HashMap<>(1);

        // 处理空值
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为为空");
            return map;
        }

        // 验证状态
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!Objects.equals(password, user.getPassword())) {
            map.put("passwordMsg", "密码错误，请重新输入");
            return map;
        }

        // 登录成功后生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUuid());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);

        // 将用户的登录凭回传给客户端
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    /**
     * 更改用户登录凭证的状态
     *
     * @param ticket 该用户的凭证值
     */
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }


    /**
     * 查询凭证对象
     *
     * @param ticket 凭证值
     * @return LoginTicket Object
     */
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    /**
     * 根据姓名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    /**
     * 更新用户头像路径
     *
     * @param userId    用户 Id
     * @param headerUrl 头像路径
     */
    public void updateHeader(int userId, String headerUrl) {
        userMapper.updateHeader(userId, headerUrl);
    }

    /**
     * 更新用户密码
     *
     * @param userId   用户 Id
     * @param password 新密码
     */
    public void updatePassword(int userId, String password) {
        userMapper.updatePassword(userId, password);
    }
}
