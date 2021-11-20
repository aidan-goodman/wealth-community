package top.aidan.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.aidan.community.controller.interceptor.LoginRequiredInterceptor;
import top.aidan.community.controller.interceptor.LoginTicketInterceptor;

/**
 * @author Aidan
 * @createTime 2021/11/17 20:19
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Configuration
public class WebMvcController implements WebMvcConfigurer {

    private final LoginRequiredInterceptor loginRequiredInterceptor;
    private final LoginTicketInterceptor loginTicketInterceptor;

    public WebMvcController(LoginTicketInterceptor loginTicketInterceptor, LoginRequiredInterceptor loginRequiredInterceptor) {
        this.loginTicketInterceptor = loginTicketInterceptor;
        this.loginRequiredInterceptor = loginRequiredInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

    }
}
