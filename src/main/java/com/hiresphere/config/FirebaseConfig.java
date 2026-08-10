package com.hiresphere.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct; // Notice: jakarta instead of javax!
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");

            if (!resource.exists()) {
                System.err.println("=========================================");
                System.err.println("ERROR: serviceAccountKey.json NOT FOUND!");
                System.err.println("Place it in: src/main/resources/serviceAccountKey.json");
                System.err.println("=========================================");
                return;
            }

            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("=========================================");
                System.out.println("Firebase Firestore Successfully Connected!");
                System.out.println("=========================================");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize Firebase:");
            e.printStackTrace();
        }
    }
}