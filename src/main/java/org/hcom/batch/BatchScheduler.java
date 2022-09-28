package org.hcom.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final BatchJobConfiguration batchJobConfiguration;

    @Scheduled(cron = "5 * * * * *")
    public void run() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("nowDate", new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))));
        JobParameters jobParameters = new JobParameters(confMap);
        try {
            jobLauncher.run(batchJobConfiguration.inactiveUserJob(), jobParameters);
            log.info("job execute!!");
        } catch(JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
            | JobParametersInvalidException | JobRestartException e) {
            log.error(e.getMessage());
        }
    }
}
