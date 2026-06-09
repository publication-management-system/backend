package com.pms.publicationmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ScrapingTaskExecutorConfig {

    @Bean(name = "scrapingTaskExecutor")
    public TaskExecutor scrapingTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(50);
        taskExecutor.setMaxPoolSize(100);
        taskExecutor.setQueueCapacity(200);

        taskExecutor.setThreadNamePrefix("scraping-executor-async-");

        return taskExecutor;
    }
}
