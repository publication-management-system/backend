package com.pms.publicationmanagement.config;

import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.utils.mapper.jackson.JacksonJsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobrunrConfig {

    @Bean
    public JobMapper jobMapper() {
        return new JobMapper(new JacksonJsonMapper());
    }

    @Bean
    public StorageProvider storageProvider(JobMapper jobMapper) {
        InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }

    @Bean
    public JobScheduler jobScheduler(StorageProvider storageProvider) {
        return new JobScheduler(storageProvider);
    }
}
