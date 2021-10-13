package top.aidan.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Aidan on 2021/10/13 15:11
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoggerTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTests.class);

    @Test
    public void loggerTest() {
        System.out.println(LOGGER.getName());
        LOGGER.info("info log");
        LOGGER.debug("debug log");
        LOGGER.trace("trace log");
    }
}
