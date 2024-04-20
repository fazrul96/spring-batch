package com.example.spring.batch.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import com.example.spring.batch.listener.JobCompletionNotificationListener;
import com.example.spring.batch.processor.ProfileItemProcessor;
import com.example.spring.batch.entity.Profile;
import com.example.spring.batch.entity.User;
import com.example.spring.batch.repository.ProfileRepository;
import com.example.spring.batch.repository.UserRepository;

@Configuration
public class BatchConfig {

    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private ProfileRepository profileRepository;


    @Bean
    public RepositoryItemReader<User> reader() {
        RepositoryItemReader<User> reader = new RepositoryItemReader<>();
        reader.setRepository(userRepository);
        reader.setMethodName("findByStatusAndEmailVerified");

        List<Object> queryMethodArguments = new ArrayList<>();
        // for status
        queryMethodArguments.add("APPROVED");
        // for emailVerified
        queryMethodArguments.add(true);

        reader.setArguments(queryMethodArguments);
        reader.setPageSize(100);
        Map<String, Direction> sorts = new HashMap<>();
        sorts.put("id", Direction.ASC);
        reader.setSort(sorts);

        return reader;
    }

    @Bean
    public RepositoryItemWriter<Profile> writer() {
        RepositoryItemWriter<Profile> writer = new RepositoryItemWriter<>();
        writer.setRepository(profileRepository);
        writer.setMethodName("save");
        return writer;
    }


    @Bean
    public ProfileItemProcessor processor() {
        return new ProfileItemProcessor();
    }


    @Bean
    public Step step1(JobRepository jobRepository, ItemReader<User> itemReader,
                      ItemWriter<Profile> itemWriter, PlatformTransactionManager transactionManager)
            throws Exception {

        int chunkSize = 5;
        return new StepBuilder("step1", jobRepository)
                .<User, Profile>chunk(chunkSize, transactionManager).reader(itemReader)
                .processor(processor()).writer(itemWriter).build();
    }

    @Bean
    public Job profileUpdateJob(JobCompletionNotificationListener listener,
                                JobRepository jobRepository, Step step1) throws Exception {

        return new JobBuilder("profileUpdateJob", jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener).start(step1).build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

}