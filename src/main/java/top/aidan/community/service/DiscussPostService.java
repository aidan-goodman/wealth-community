package top.aidan.community.service;

import org.springframework.stereotype.Service;
import top.aidan.community.dao.DiscussPostMapper;
import top.aidan.community.entity.DiscussPost;

import java.util.List;

/**
 * Created by Aidan on 2021/10/13 14:57
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@Service
public class DiscussPostService {

    private final DiscussPostMapper discussPostMapper;

    public DiscussPostService(DiscussPostMapper discussPostMapper) {
        this.discussPostMapper = discussPostMapper;
    }

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

}
