package top.aidan.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// 这些注解表示启用了 Spring 的线程池，并且开启定时任务
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
