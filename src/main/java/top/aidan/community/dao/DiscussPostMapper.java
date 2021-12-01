package top.aidan.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.aidan.community.entity.DiscussPost;

import java.util.List;

/**
 * Created by Aidan on 2021/10/11 20:47
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 *
 * @author Aidan
 */

@Mapper
public interface DiscussPostMapper {

    /**
     * 实现分页查询
     *
     * @param userId 为 0 表示所有
     * @param offset 起始行
     * @param limit  每页限制
     * @return 所有帖子
     */
    List<DiscussPost> selectDiscussPosts(
            @Param("userId") int userId,
            @Param("offset") int offset,
            @Param("limit") int limit);


    /**
     * `@Param(strValue)` 注解用于给参数取字符串别名，加上比较保险
     * sql 在 <if> 里使用，则所有参数必须加别名
     *
     * @param userId 根据 userId 判断行数（默认为 0，寻找所有）
     * @return 返回所有帖子数或单个用户的所有帖子
     */
    int selectDiscussPostRows(
            @Param("userId") int userId);

    /**
     * 进行发帖时帖子的插入
     *
     * @param discussPost 对应的帖子对象
     * @return result
     */
    int insertDiscussionPost(DiscussPost discussPost);


}
