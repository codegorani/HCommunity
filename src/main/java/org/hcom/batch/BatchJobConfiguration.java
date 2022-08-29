package org.hcom.batch;

import lombok.RequiredArgsConstructor;
import org.hcom.models.user.User;
import org.hcom.models.user.enums.UserStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@SpringBootConfiguration
public class BatchJobConfiguration {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job inactiveUserJob(JobBuilderFactory jobBuilderFactory, Step inactiveJobStep) {
        return jobBuilderFactory.get("inactiveUserJob")
                .start(inactiveJobStep)
                .build();
    }

    @Bean
    public Step inactiveJobStep(StepBuilderFactory stepBuilderFactory,
                                JpaPagingItemReader<User> inactiveUserReader) {
        return stepBuilderFactory.get("inactiveJobStep")
                .<User, User> chunk(10)
                .reader(inactiveUserReader)
                .processor(inactiveUserProcessor())
                .writer(inactiveUserWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<User> inactiveUserReader(@Value("${batch.parameter.nowDate}") String nowDate) {
        JpaPagingItemReader<User> jpaPagingItemReader = new JpaPagingItemReader<>();
        String sql = "SELECT u FROM User u " +
                "WHERE u.lastLoginTime < :lastLoginTime " +
                "AND u.userStatus = :userStatus";
        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.of(
                Integer.parseInt(nowDate.substring(0, 4)),
                Integer.parseInt(nowDate.substring(4, 6)),
                Integer.parseInt(nowDate.substring(6, 8)), 0, 0);
        map.put("lastLoginTime", now.minusMonths(3));
        map.put("userStatus", UserStatus.ACTIVE);
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
        jpaPagingItemReader.setPageSize(10);
        jpaPagingItemReader.setQueryString(sql);
        jpaPagingItemReader.setParameterValues(map);
        return jpaPagingItemReader;
    }

    public ItemProcessor<User, User> inactiveUserProcessor() {
        return User::inactive;
    }

    public JpaItemWriter<User> inactiveUserWriter() {
        JpaItemWriter<User> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
