package top.aidan.community.service;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import top.aidan.community.dao.DiscussPostMapper;
import top.aidan.community.entity.DiscussPost;
import top.aidan.community.util.SensitiveFilter;

import java.util.List;

/**
 * Created by Aidan on 2021/10/13 14:57
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 * @author Aidan
 */

@Service
public class DiscussPostService {

    private final DiscussPostMapper discussPostMapper;

    private final SensitiveFilter sensitiveFilter;

    public DiscussPostService(DiscussPostMapper discussPostMapper, SensitiveFilter sensitiveFilter) {
        this.discussPostMapper = discussPostMapper;
        this.sensitiveFilter = sensitiveFilter;
    }

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 调用 DAO 层进行数据的持久化
     *
     * @param post 已封装的 DicussPost 对象
     * @return result
     */
    public int addDiscussionPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        // 设置 HTML 转义，防止样式影响显示结果
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 进行敏感词过滤
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussionPost(post);
    }


    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    public void updateCommentCount(int id, int commentCount) {
        discussPostMapper.updateCommentCount(id, commentCount);
    }
}
