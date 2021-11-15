package top.aidan.community.controller;

import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.aidan.community.entity.User;
import top.aidan.community.service.UserService;
import top.aidan.community.util.CommunityConstant;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @author Aidan
 * @createTime 2021/11/11 10:59
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;

    /**
     * 注入之前定义的 Bean
     */
    private final Producer kaptchaProducer;

    public LoginController(UserService userService, Producer kaptchaProducer) {
        this.userService = userService;
        this.kaptchaProducer = kaptchaProducer;
    }

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 注册页面的展示
     *
     * @return Register page
     */
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "site/register";
    }

    /**
     * 发起注册请求
     *
     * @param model 封装需要显示的数据
     * @param user  根据用户提交的信息生成对象，输入框字段与 field 保持一致
     * @return 注册成功返回 result Page，否则回到 register page 并显示错误信息
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> message = userService.register(user);
        if (message == null || message.isEmpty()) {
            model.addAttribute("msg", "您已注册成功，请查看邮箱并根据提示进行下一步操作");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        } else {
            model.addAttribute("usernameMsg", message.get("usernameMsg"));
            model.addAttribute("passwordMsg", message.get("passwordMsg"));
            model.addAttribute("emailMsg", message.get("emailMsg"));
            return "site/register";
        }

    }

    /**
     * 用户激活方法
     * formAt(URL):http://localhost:8080/community/activation/101/code
     *
     * @param model  封装结果信息
     * @param userId 根据路径获取被激活用户的 ID
     * @param code   根据路径获取当前激活码
     * @return 返回 result page
     */
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "您已成功激活，现在可以正常登录使用");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "您已激活成功，无需重复激活");
            model.addAttribute("target", "/login");
        } else {
            model.addAttribute("msg", "激活失败，请提供正确的激活码");
            model.addAttribute("target", "/index");
        }
        return "site/operate-result";
    }

    /**
     * 验证码生成方法
     *
     * @param response 做中转输出
     * @param session  存储验证码信息
     */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入 Session，考虑安全性
        session.setAttribute("kaptcha", text);

        // 将图片输出到浏览器
        response.setContentType("image/png");

        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("验证码响应失败，" + e.getMessage());
        }
    }

    /**
     * 请求登录页面
     *
     * @return login page
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "site/login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberMe,
                        Model model, HttpSession session, HttpServletResponse response) {

        // 获取当前页面的验证码
        String kaptcha = (String) session.getAttribute("kaptcha");

        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "/site/login";
        }

        // 检查账号密码
        int expiredSeconds = rememberMe ? REMEMBER_EXPIRE_SECONDS : DEFAULT_EXPIRE_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);

        String cookieName = "ticket";
        if (map.containsKey(cookieName)) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "site/login";
        }

    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }

}
