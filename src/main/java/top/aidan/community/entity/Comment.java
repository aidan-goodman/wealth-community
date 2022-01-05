package top.aidan.community.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Aidan
 * @createTime 2022/1/5 20:47
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Data
@ToString
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;


}
