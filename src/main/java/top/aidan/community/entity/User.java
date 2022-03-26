package top.aidan.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by Aidan on 2021/10/13 14:55
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 *
 * @author Aidan
 */

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;

    /**
     * 0-普通用户
     * 1-版主
     * 2-管理员
     */
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
