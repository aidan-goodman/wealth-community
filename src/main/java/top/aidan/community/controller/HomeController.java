package top.aidan.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.aidan.community.entity.DiscussPost;
import top.aidan.community.entity.Page;
import top.aidan.community.entity.User;
import top.aidan.community.service.DiscussPostService;
import top.aidan.community.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aidan
 * @createTime 2021/11/11 10:11
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Controller
public class HomeController {

    private final DiscussPostService discussPostService;
    private final UserService userService;

    public HomeController(DiscussPostService discussPostService,
                          UserService userService) {
        this.discussPostService = discussPostService;
        this.userService = userService;
    }

    /**
     * 使用分页查询显示首页
     *
     * @param model 存取数据
     * @param page  页信息，可以封装到 model
     * @return Model And View
     */
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        // 此处的 Page 可以直接注入到 Model
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0,
                page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                // 将发帖人信息进行保存
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }

}

