package com.hiresphere.model;

import lombok.Data;

@Data
public class JobApplication {
    private String applicationId;
    private String studentName;
    private String studentBranch;
    private Double studentCgpa;
    private String jobId;
    private String status; // APPLIED, HIRED, REJECTED
}