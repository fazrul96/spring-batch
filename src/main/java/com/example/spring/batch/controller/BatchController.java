package com.example.spring.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/batch")
public class BatchController {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @PostMapping(path = "/start")
    public ResponseEntity<?> startBatch() {
        JobExecution jobExecution = null;
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("uniqueId", System.currentTimeMillis()).toJobParameters();

            jobExecution = jobLauncher.run(job, jobParameters);

        } catch (JobExecutionAlreadyRunningException | JobRestartException
                 | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(jobExecution.getStatus());
    }

}