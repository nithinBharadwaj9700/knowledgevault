# 📘 Knowledge Vault – Spring Boot REST API

A lightweight, secure Spring Boot REST API to store and retrieve different types of knowledge entries using polymorphic persistence.

---

## 🛠 TechStack

- **Java**: 17
- **Spring Boot**: 3.x
- **Spring Security**: HTTP Basic
- **Spring Data JPA**
- **H2 Database** 
- **Maven**
- **JUnit 5 + MockMvc**

---

## 📦 Features

- RESTful API with proper HTTP verbs and status codes
- Polymorphic knowledge model (Text, Link, Quote, Nested)
- HTTP Basic authentication with role-based authorization
- Centralized exception handling
- Pagination support
- File-based logging
- Automated tests (service + controller + security)

---

## 🚀 How to Run the Application

### Prerequisites
- JDK 17+
- Maven 3.8+

---

## 📡 API Endpoints

### Public Endpoints
### Public Endpoints
- `GET /api/knowledge/{id}` – Get knowledge by ID
- `GET /api/knowledge?page=0&size=5` – List knowledge with pagination
- `GET /api/knowledge/recent?days=7` – Get knowledge created in the last N days (default: 7)


### Secured Endpoints
- `POST /api/knowledge` – Create knowledge (authentication required)
- `DELETE /api/knowledge/{id}` – Delete knowledge (ADMIN only)

----
### Run locally
```bash
./mvnw spring-boot:run
