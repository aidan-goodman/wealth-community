package top.aidan.community.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.aidan.community.entity.AidanTest;

/**
 * Created by Aidan on 2021/10/4 17:48
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 * @author Aidan
 */

@RestController
@RequestMapping("/test")
public class HelloTest {

    @RequestMapping("/hello")
    public String hello(AidanTest aidan,String name) {
        return "hello "+aidan.getName()+", welcome Community\nname: "+name;
    }
}
