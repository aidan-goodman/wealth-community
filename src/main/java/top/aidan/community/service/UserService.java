package top.aidan.community.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.aidan.community.dao.UserMapper;
import top.aidan.community.entity.User;
import top.aidan.community.util.CommunityConstant;
import top.aidan.community.util.CommunityUtil;
import top.aidan.community.util.MailClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public UserService(UserMapper userMapper, MailClient mailClient, TemplateEngine templateEngine) {
        this.userMapper = userMapper;
        this.mailClient = mailClient;
        this.templateEngine = templateEngine;
    }

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

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
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
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

}
