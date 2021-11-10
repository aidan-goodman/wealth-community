package top.aidan.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.aidan.community.util.MailClient;

/**
 * Created by Aidan on 2021/11/10 19:30
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("aidan0304@foxmail.com", "Test", "welcome");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "Aidan");

        String content = templateEngine.process("/mail/demo", context);
        // System.out.println(content);

        mailClient.sendMail("aidan0304@foxmail.com", "HTMLTest", content);

    }

}
