package com.hiresphere.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) {
                System.out.println("Firebase already initialized.");
                return;
            }

            InputStream serviceAccount;
            String envCredentials = System.getenv("FIREBASE_CREDENTIALS");

            if (envCredentials != null && !envCredentials.isBlank()) {
                // Read from Render Environment Variable
                serviceAccount = new ByteArrayInputStream(envCredentials.getBytes());
                System.out.println("Loading Firebase credentials from Environment Variable...");
            } else {
                // Fallback to local classpath file for local development
                ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
                if (!resource.exists()) {
                    System.err.println("CRITICAL ERROR: Neither FIREBASE_CREDENTIALS env var nor serviceAccountKey.json was found!");
                    return;
                }
                serviceAccount = resource.getInputStream();
                System.out.println("Loading Firebase credentials from local classpath file...");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("=========================================");
            System.out.println("Firebase Firestore Successfully Connected!");
            System.out.println("=========================================");

        } catch (Exception e) {
            System.err.println("FAILED TO INITIALIZE FIREBASE ADMIN SDK:");
            e.printStackTrace();
        }
    }
}