package com.hiresphere.model;

import lombok.Data;

@Data
public class JobPosting {
    private String id;
    private String title;
    private String companyName;
    private Double minCgpa;
    private String targetBranch;
}