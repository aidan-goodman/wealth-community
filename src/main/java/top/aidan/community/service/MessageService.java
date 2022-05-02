package top.aidan.community.service;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import top.aidan.community.dao.MessageMapper;
import top.aidan.community.entity.Message;
import top.aidan.community.util.SensitiveFilter;

import java.util.List;

/**
 * @author Aidan
 * @createTime 2022/5/2 11:59
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Service
public class MessageService {
    private final MessageMapper messageMapper;
    private final SensitiveFilter sensitiveFilter;

    public MessageService(MessageMapper messageMapper, SensitiveFilter sensitiveFilter) {
        this.messageMapper = messageMapper;
        this.sensitiveFilter = sensitiveFilter;
    }

    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }
}
