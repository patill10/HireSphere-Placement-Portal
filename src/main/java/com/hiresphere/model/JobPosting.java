package com.hiresphere.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobPosting {
    private String id;

    @NotBlank(message = "Job title cannot be blank")
    private String title;

    @NotBlank(message = "Company name cannot be blank")
    private String companyName;

    @NotNull(message = "Minimum CGPA is required")
    @DecimalMin(value = "0.0", message = "CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "CGPA cannot exceed 10.0")
    private Double minCgpa;

    @NotBlank(message = "Target branch cannot be blank")
    private String targetBranch;
}