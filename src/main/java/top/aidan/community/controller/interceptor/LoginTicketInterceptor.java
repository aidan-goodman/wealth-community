package top.aidan.community.controller.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.aidan.community.entity.LoginTicket;
import top.aidan.community.entity.User;
import top.aidan.community.service.UserService;
import top.aidan.community.util.CookieUtil;
import top.aidan.community.util.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author Aidan
 * @createTime 2021/11/17 19:31
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final HostHolder hostHolder;

    public LoginTicketInterceptor(UserService userService, HostHolder hostHolder) {
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从 Cookie 中获取 Ticket
        String cookieName = "ticket";
        String ticket = CookieUtil.getValue(request, cookieName);

        if (ticket != null) {
            // 根据凭证查询对象
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().isAfter(LocalDateTime.now())) {
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户
                hostHolder.setUser(user);

            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        hostHolder.clear();
    }
}
