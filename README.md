# Expense Tracker (Backend)

## 📌 Project Overview
This repository contains the backend implementation of the **Expense Tracker** application built using **Java** and **Spring Boot**. It provides secure APIs for user authentication, expense logging, and data retrieval, with role-based access control using **JWT** and **Spring Security**.

## 📌 Features
✔️ User Registration & Login  
✔️ JWT-based Authentication  
✔️ Role-Based Access Control (User/Admin)  
✔️ Secure Endpoints using Spring Security  
✔️ Users can log their monthly expenses  
✔️ Fetch all expenses for the logged-in user  
✔️ Filter expenses by month or year  
✔️ Admin can update user roles (e.g., promote to admin)  
✔️ Clean error handling and logging

## 🛠️ Tech Stack
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security 6**
- **JWT (Java JWT - jjwt)**
- **Maven**
- **MySQL**
- **Postman** (for testing)

### 📦 Spring Boot Dependencies
- **spring-boot-starter-web** – REST APIs  
- **spring-boot-starter-security** – Authentication & Authorization  
- **spring-boot-starter-data-jpa** – Database interaction  
- **jjwt-api, jjwt-impl, jjwt-jackson** – JWT token generation and validation  
- **spring-boot-devtools** – Development-time utilities  
- **lombok** – Reduces boilerplate code

## 🔐 Authentication & Security

### 🔑 JWT Package Overview
- **JwtUtil.java** – Utility for creating and validating JWTs  
- **JwtFilter.java** – Filters requests and sets authentication context from JWT  
- **CustomUserDetailsService.java** – Loads user credentials from the database for Spring Security  
- **SecurityConfig.java** – Spring Security configuration with JWT filter and role-based access

## 📖 API ENDPOINT GUIDE

### 👥 Public Endpoints
- **`POST /user/signup`** – Register a new user  
- **`POST /user/login`** – Authenticate user and receive JWT token

### 🔐 Secured Endpoints (require JWT token)

#### 🧾 Expenses
- **`POST /expense/add`** – Add a new expense  
- **`GET /expense/all`** – Get all expenses for the logged-in user  
- **`GET /expense/month/{month}`** – Get expenses by month  
- **`GET /expense/year/{year}`** – Get expenses by year

#### ⚙️ Admin-only
- **`PUT /admin/set-role/{userId}`** – Promote user to admin

## Spring configurations for the project: 
### add the following configurations in the "/src/main/resources/application.properties" file:
```properties
# Application Info
spring.application.name=expense-tracker

# Server Configuration
server.port=9999

# Database Configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver        # tells Spring Boot to use the MySQL Connector/J (JDBC driver)
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker   # configuration key to set the JDBC URL
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# JPA & Hibernate Settings
spring.jpa.show-sql=true                         # shows the SQL query 
spring.jpa.hibernate.ddl-auto=update             # automatically updates your database schema
spring.jpa.properties.hibernate.format_sql=true  # formatting the SQL query in readable form
```


## 📂 Package Structure
```yaml
📦 com.stew.expense_tracker
├── 📂 config              # Spring Security Configuration
├── 📂 constants           # Enum for user roles
├── 📂 controller          # REST controllers
├── 📂 model               # Entity classes (User, Expense)
├── 📂 repository          # JPA Repositories
├── 📂 service             # Service classes with business logic
├── 📂 jwt                 # JWT utilities and filter
├── 📂 wrapper             # DTOs for requests/responses
└── 📄 Application.java    # Main Spring Boot class
