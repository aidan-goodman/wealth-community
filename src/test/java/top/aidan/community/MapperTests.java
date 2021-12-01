package top.aidan.community;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.aidan.community.dao.DiscussPostMapper;
import top.aidan.community.dao.LoginTicketMapper;
import top.aidan.community.dao.UserMapper;
import top.aidan.community.entity.DiscussPost;
import top.aidan.community.entity.LoginTicket;
import top.aidan.community.entity.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Aidan on 2021/10/4 19:13
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liuBei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("aidan");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("https://images.nowcoder.com/images/20210924/1030032960_1632464487707/64188F08D61F98C9EE5212F58672A8F4?x-oss-process=image/resize,m_mfit,h_200,w_200");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "https://images.nowcoder.com/images/20201031/157517829_1604126433282_605475850F555A9A1D76953CFB3E39A6?x-oss-process=image/resize,m_mfit,h_200,w_200");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "hello");
        System.out.println(rows);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(154);
        loginTicket.setTicket("aidanTicket");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        int i=loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket aidanTicket = loginTicketMapper.selectByTicket("aidanTicket");
        System.out.println(aidanTicket);
    }

    @Test
    public void testUpdateLoginTicketStatus() {
        int resultCount = loginTicketMapper.updateStatus("aidanTicket", 1);
        System.out.println(resultCount);
    }

    @Test
    public void insertDiscussionPost() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(155);
        discussPost.setTitle("测试");
        discussPost.setContent("测试内容");
        int i = discussPostMapper.insertDiscussionPost(discussPost);
        System.out.println(i);
    }

}

