package top.aidan.community.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    //0-普通用户    1-版主    2-管理员
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

}
