package com.hiresphere.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.hiresphere.model.JobApplication;
import com.hiresphere.model.JobPosting;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class JobService {

    // 1. Recruiter creates a job
    public String createJob(JobPosting job) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        String jobId = UUID.randomUUID().toString();
        job.setId(jobId);
        
        ApiFuture<WriteResult> result = db.collection("jobs").document(jobId).set(job);
        return "Job Created at: " + result.get().getUpdateTime().toString();
    }

    // 2. Student applies with Eligibility Verification Logic
    public String applyForJob(JobApplication application) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // Fetch job requirements from Firestore
        DocumentSnapshot jobDoc = db.collection("jobs").document(application.getJobId()).get().get();
        if (!jobDoc.exists()) {
            return "Failed: Job posting not found.";
        }

        JobPosting job = jobDoc.toObject(JobPosting.class);

        // Eligibility Check (Core Logic)
        if (application.getStudentCgpa() < job.getMinCgpa()) {
            return "Ineligible: Student CGPA (" + application.getStudentCgpa() + 
                   ") is below minimum requirement (" + job.getMinCgpa() + ").";
        }

        // Save application if eligible
        String appId = UUID.randomUUID().toString();
        application.setApplicationId(appId);
        application.setStatus("APPLIED");

        db.collection("applications").document(appId).set(application);
        return "Application Successful! Status: APPLIED";
    }
}