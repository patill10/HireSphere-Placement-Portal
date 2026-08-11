package com.hiresphere.controller;

import com.hiresphere.model.JobApplication;
import com.hiresphere.model.JobPosting;
import com.hiresphere.service.JobService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    public String createJob(@Valid @RequestBody JobPosting job) throws Exception {
        return jobService.createJob(job);
    }

    @PostMapping("/applications")
    public String applyForJob(@Valid @RequestBody JobApplication application) throws Exception {
        return jobService.applyForJob(application);
    }
}