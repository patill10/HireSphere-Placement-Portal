# 🎓 HireSphere — Smart Campus Placement Portal

A full-stack, cloud-native recruitment and campus placement application built with **Java 21**, **Spring Boot 3**, and **Google Firebase Firestore**. The system automates student eligibility verification based on academic criteria (CGPA/Branch) prior to application submission.

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=springboot)
![Firebase](https://img.shields.io/badge/Firebase-Firestore-FFCA28?style=for-the-badge&logo=firebase)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap)

---

## 🚀 Key Features

* **Cloud NoSQL Storage:** Integrated with Google Firebase Firestore for real-time document storage without local database overhead.
* **Automated Eligibility Verification:** Core business logic evaluates student eligibility parameters (CGPA requirements) in real time before persisting application data.
* **Input Validation & Exception Handling:** Enforces field validation using `jakarta.validation` and returns structured error responses using `@RestControllerAdvice`.
* **Single-Page Dashboard UI:** Built-in web interface for recruiters to post jobs and students to apply.

---

## 🏗️ System Architecture & Data Flow

```text
[ Client (Browser / Postman) ]
            │
            ▼  HTTP POST Requests (JSON)
[ REST Controller Layer (JobController) ]
            │
            ▼  Validation & Method Invocation
[ Service Layer (JobService) ]
            │
            ├── Check CGPA Eligibility Requirement
            │     ├── Ineligible ──> Returns 400 Bad Request
            │     └── Eligible ────> Proceed to Database
            ▼
[ Google Firebase Admin SDK ]
            │
            ▼  Cloud Connection
[ Firebase Cloud Firestore Database ]
