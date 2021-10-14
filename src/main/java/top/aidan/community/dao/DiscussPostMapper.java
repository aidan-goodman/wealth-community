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

    // orderMode 排序模式，默认为 0,1 是按帖子热度排序
    List<DiscussPost> selectDiscussPosts(
            @Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);


    /**
     * `@Param(strValue)` 注解用于给参数取字符串别名，加上比较保险
     * sql 在 <if> 里使用，则所有参数必须加别名
     * @param userId 根据 userId 判断行数（默认为 0，寻找所有）
     * @return 返回所有帖子数或单个用户的所有帖子
     */
    int selectDiscussPostRows(@Param("userId") int userId);


}
