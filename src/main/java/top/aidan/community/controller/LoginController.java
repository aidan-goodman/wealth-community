package top.aidan.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.aidan.community.entity.User;
import top.aidan.community.service.UserService;
import top.aidan.community.util.CommunityConstant;

import java.util.Map;

/**
 * @author Aidan
 * @createTime 2021/11/11 10:59
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Controller
public class LoginController implements CommunityConstant {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "site/register";
    }

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
     * URL:http://localhost:8080/community/activation/101/code
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

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "site/login";
    }


}
