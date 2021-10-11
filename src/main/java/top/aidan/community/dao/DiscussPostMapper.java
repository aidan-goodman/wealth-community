package top.aidan.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.aidan.community.entity.DiscussPost;

import java.util.List;

/**
 * Created by Aidan on 2021/10/11 20:47
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@Mapper
public interface DiscussPostMapper {
    // orderMode 排序模式，默认为 0;1 是按帖子热度排序
    List<DiscussPost> selectDiscussPosts(
            @Param("userId") int userId, @Param("offset") int offset,
            @Param("limit") int limit, @Param("orderMode") int orderMode);

    // @Param 注解用于给参数取别名，只有一个变量可以不加，要是有两个一定要加，所以一般都加上比较保险
    // sql 在 <if> 里使用,则所有参数必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(@Param("id") int id);

    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    int updateType(@Param("id") int id, @Param("type") int type);

    int updateStatus(@Param("id") int id, @Param("status") int status);

    int updateScore(@Param("id") int id, @Param("score") double score);
}