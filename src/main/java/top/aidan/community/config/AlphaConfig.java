package top.aidan.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * Created by Aidan on 2021/10/11 20:45
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@Configuration
public class AlphaConfig {
    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

}
