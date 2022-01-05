package top.aidan.community.util;

/**
 * @author Aidan
 * @createTime 2021/11/11 10:41
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态下的登录超时时间
     */
    int DEFAULT_EXPIRE_SECONDS = 3600 * 12;

    /**
     * 记住状态下的超时时间
     */
    int REMEMBER_EXPIRE_SECONDS = 3600 * 24 * 100;


    /**
     * 实体类型: 帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型: 评论
     */
    int ENTITY_TYPE_COMMENT = 2;


}
