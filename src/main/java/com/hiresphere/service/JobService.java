package com.hiresphere.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.hiresphere.model.JobApplication;
import com.hiresphere.model.JobPosting;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class JobService {

    // 1. Create a Job Posting
    public String createJob(JobPosting job) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        String jobId = UUID.randomUUID().toString();
        job.setId(jobId);
        
        ApiFuture<WriteResult> result = db.collection("jobs").document(jobId).set(job);
        return "Job Created Successfully! Job ID: " + jobId;
    }

    // 2. Retrieve All Active Job Postings
    public List<JobPosting> getAllJobs() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection("jobs").get();
        QuerySnapshot querySnapshot = query.get();
        
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        List<JobPosting> jobList = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            JobPosting job = document.toObject(JobPosting.class);
            job.setId(document.getId());
            jobList.add(job);
        }

        return jobList;
    }

    // 3. Apply for Job with Atomic Transaction & Duplicate Check
    public String applyForJob(JobApplication application) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        return db.runTransaction(transaction -> {
            // Fetch Job details atomically
            DocumentReference jobRef = db.collection("jobs").document(application.getJobId());
            DocumentSnapshot jobDoc = transaction.get(jobRef).get();

            if (!jobDoc.exists()) {
                return "Failed: Job posting not found.";
            }

            JobPosting job = jobDoc.toObject(JobPosting.class);

            // Check CGPA Eligibility Requirement
            if (job != null && application.getStudentCgpa() < job.getMinCgpa()) {
                return "Ineligible: Student CGPA (" + application.getStudentCgpa() + 
                       ") is below minimum requirement (" + job.getMinCgpa() + ").";
            }

            // Create deterministic App ID to prevent duplicate applications
            String sanitizedStudentName = application.getStudentName().replaceAll("\\s+", "_").toLowerCase();
            String appId = sanitizedStudentName + "_" + application.getJobId();

            DocumentReference appRef = db.collection("applications").document(appId);
            DocumentSnapshot appSnapshot = transaction.get(appRef).get();

            if (appSnapshot.exists()) {
                return "Failed: You have already applied for this job posting.";
            }

            // Save application
            application.setApplicationId(appId);
            application.setStatus("APPLIED");
            transaction.set(appRef, application);

            return "Application Successful! Status: APPLIED";
        }).get();
    }
}