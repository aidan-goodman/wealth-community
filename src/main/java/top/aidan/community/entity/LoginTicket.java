package top.aidan.community.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Aidan
 * @createTime 2021/11/15 17:11
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Data
public class LoginTicket {

    private int id;
    private int userId;
    private String ticket;
    private int status;
    private LocalDateTime expired;

}
