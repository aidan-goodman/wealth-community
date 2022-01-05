package top.aidan.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by Aidan on 2021/10/11 20:36
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@Data
public class DiscussPost {

    private int id;

    private int userId;

    private String title;

    private String content;

    private int type;

    private int status;

    private Date createTime;

    private int commentCount;

    private double score;
}
