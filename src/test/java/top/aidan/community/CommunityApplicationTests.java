package top.aidan.community;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.aidan.community.util.CommunityUtil;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests {

    @Test
    void contextLoads() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Aidan");
        map.put("age", 21);
        System.out.println(CommunityUtil.getJsonString(0, "ok", map));
    }

}
