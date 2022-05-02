package top.aidan.community.dao;

import org.apache.ibatis.annotations.Mapper;
import top.aidan.community.entity.Message;

import java.util.List;

/**
 * @author Aidan
 * @createTime 2022/4/7 22:47
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Mapper
public interface MessageMapper {
    /**
     * 查询当前用户的会话列表
     *
     * @param userId 用户 ID
     * @param offset 起始
     * @param limit  限定
     * @return 针对每个会话只返回一条最新的私信
     */
    List<Message> selectConversations(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话数量
     *
     * @param userId 用户 ID
     * @return 当前的会话总值
     */
    int selectConversationCount(int userId);

    /**
     * 查询某个会话所包含的私信列表
     *
     * @param conversationId 会话 ID
     * @param offset         off
     * @param limit          count
     * @return 会话列表
     */
    List<Message> selectLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量
     *
     * @param conversationId 会话 ID
     * @return 私信数量
     */
    int selectLetterCount(String conversationId);

    /**
     * 查询未读私信的数量
     *
     * @param userId         用户 ID
     * @param conversationId 会话 ID
     * @return 未读数量
     */
    int selectLetterUnreadCount(int userId, String conversationId);
}