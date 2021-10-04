package top.aidan.community.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Aidan on 2021/10/4 17:48
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@RestController
@RequestMapping("/test")
public class HelloTest {

    @RequestMapping("/hello")
    public String hello() {
        return "hello Community";
    }
}
