package top.aidan.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.aidan.community.entity.Comment;
import top.aidan.community.entity.DiscussPost;
import top.aidan.community.entity.Page;
import top.aidan.community.entity.User;
import top.aidan.community.service.CommentService;
import top.aidan.community.service.DiscussPostService;
import top.aidan.community.service.UserService;
import top.aidan.community.util.CommunityConstant;
import top.aidan.community.util.CommunityUtil;
import top.aidan.community.util.HostHolder;

import java.util.*;

/**
 * @author Aidan
 * @createTime 2021/12/1 14:44
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    private final DiscussPostService discussPostService;
    private final HostHolder hostHolder;
    private final UserService userService;
    private final CommentService commentService;

    public DiscussPostController(DiscussPostService discussPostService, HostHolder hostHolder, UserService userService,
                                 CommentService commentService) {
        this.discussPostService = discussPostService;
        this.hostHolder = hostHolder;
        this.userService = userService;
        this.commentService = commentService;
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
    public String getDiscussionPost(@PathVariable(name = "discussionPostId") int discussionPostId, Model model,
                                    Page page) {
        DiscussPost post = discussPostService.findDiscussPostById(discussionPostId);
        model.addAttribute("post", post);

        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        page.setPath("/discuss/detail/" + discussionPostId);
        page.setRows(post.getCommentCount());
        page.setLimit(5);

        // 评论列表（帖子的评论）
        List<Comment> commentList = commentService.findCommentsByEntity(
                CommunityConstant.ENTITY_TYPE_POST, discussionPostId, page.getOffset(), page.getLimit());

        List<Map<String, Object>> commentViewObjectList = new ArrayList<>();
        if (!commentList.isEmpty()) {
            for (Comment comment : commentList) {
                Map<String, Object> commentVo = new HashMap<>(30);

                // 评论的实质内容
                commentVo.put("comment", comment);
                // 评论的作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 查找回复（评论的评论）
                List<Comment> replyList = commentService.findCommentsByEntity(CommunityConstant.ENTITY_TYPE_COMMENT,
                        comment.getId(), 0, Integer.MAX_VALUE);
                // 回复的 VO 列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>(12);
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 是否存在回复的目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentViewObjectList.add(commentVo);

            }
        }

        // 最终形式：List<Map<String, Object>> And List<Map<String, List<Map<String, Object>>>>
        model.addAttribute("comments", commentViewObjectList);

        return "/site/discuss-detail";

    }
}
