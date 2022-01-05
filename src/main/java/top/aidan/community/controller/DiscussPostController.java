package top.aidan.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.aidan.community.entity.DiscussPost;
import top.aidan.community.entity.User;
import top.aidan.community.service.DiscussPostService;
import top.aidan.community.service.UserService;
import top.aidan.community.util.CommunityUtil;
import top.aidan.community.util.HostHolder;

import java.util.Date;

/**
 * @author Aidan
 * @createTime 2021/12/1 14:44
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    private final DiscussPostService discussPostService;
    private final HostHolder hostHolder;
    private final UserService userService;

    public DiscussPostController(DiscussPostService discussPostService, HostHolder hostHolder, UserService userService) {
        this.discussPostService = discussPostService;
        this.hostHolder = hostHolder;
        this.userService = userService;
    }

    /**
     * 发帖功能的控制层处理
     *
     * @param title   帖子标题
     * @param content 帖子正文
     * @return result
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussionPost(String title, String content) {
        // 获取登陆的用户对象
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJsonString(403, "未登录用户不能发帖");
        }

        // 数据填充
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());

        // 成功则返回结果，如果失败后续统一进行异常管理
        return discussPostService.addDiscussionPost(post) > 0 ?
                CommunityUtil.getJsonString(200, "帖子发布成功") :
                CommunityUtil.getJsonString(500, "服务器错误");

    }

    @RequestMapping(path = "/detail/{discussionPostId}", method = RequestMethod.GET)
    public String getDiscussionPost(@PathVariable(name = "discussionPostId") int discussionPostId, Model model) {
        DiscussPost post = discussPostService.findDiscussPostById(discussionPostId);
        model.addAttribute("post", post);

        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

        return "/site/discuss-detail";

    }
}
