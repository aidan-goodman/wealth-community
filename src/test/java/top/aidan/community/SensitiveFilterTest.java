package top.aidan.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.aidan.community.util.SensitiveFilter;

/**
 * @author Aidan
 * @createTime 2021/11/30 13:39
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveFilterTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void filter() {
        String str = "这里可以赌¥¥¥¥博，可以￥嫖&娼";


        String filter = sensitiveFilter.filter(str);

        System.out.println(filter);

    }
}
