package top.aidan.community.service;

import org.springframework.stereotype.Service;
import top.aidan.community.dao.CommentMapper;
import top.aidan.community.entity.Comment;

import java.util.List;

/**
 * @author Aidan
 * @createTime 2022/1/5 21:02
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Service
public class CommentService {

    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

}
