package top.aidan.community.dao;

import org.apache.ibatis.annotations.Mapper;
import top.aidan.community.entity.Comment;

import java.util.List;

/**
 * @author Aidan
 * @createTime 2022/1/5 20:52
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Mapper
public interface CommentMapper {
    /**
     * 根据实体进行评论的查询（帖子的评论或是评论的评论）
     *
     * @param entityType 实体的类型
     * @param entityId   实体的 Id
     * @param offset     分页起始
     * @param limit      分页限制
     * @return 一组评论
     */
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 查询某个实体的评论总数
     *
     * @param entityType 实体的类型
     * @param entityId   实体 Id
     * @return Int 型总数
     */
    int selectCountByEntity(int entityType, int entityId);
}
