package top.aidan.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Aidan on 2021/10/13 14:40
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 * @author Aidan
 */

// 这些注解表示启用了 Spring 的线程池，并且开启定时任务

@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
