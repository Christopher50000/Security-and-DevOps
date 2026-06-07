# eCommerce Application (Spring Boot Security & DevOps Project)

## Overview
This project is a Java-based eCommerce backend built with **Spring Boot**, **Hibernate ORM**, and an **H2 in-memory database**. The main focus of this project is implementing secure authentication and authorization so users can only access their own data using **JWT-based security**.

It also demonstrates backend development and DevOps skills including CI/CD concepts, API testing, cloud deployment practices, and secure application design.

---

## Tech Stack
- Java
- Spring Boot
- Spring Security
- Hibernate / JPA
- H2 Database
- JSON Web Tokens (JWT)
- Maven

---

## Project Structure

### `model.persistence`
Contains JPA entity models persisted in the database:
- Cart – stores user cart items
- Item – defines store items
- User – stores user account information
- UserOrder – stores submitted orders

### `model.persistence.repositories`
JPA repositories used to interact with the database and perform CRUD operations.

### `model.requests`
Request/DTO models used for API input handling and JSON mapping.

### `controllers`
REST API endpoints for the application. Each model has its own controller.

### `resources`
- `application.properties` – database and Hibernate configuration
- `data.sql` – seed data loaded at application startup
- `api-requests.http` – API testing for local and EC2 file
---

## Running the Application

1. Import the project into your IDE (Eclipse or IntelliJ recommended)
2. Run the Spring Boot application:
   - Right-click project → Run As → Spring Boot Application
3. The application will start at:
   - http://localhost:8080
4. Use a REST client (such as Postman) to test endpoints.

---

## API Example

### Create User

**POST**
-- http://localhost:8080/api/user/create


#### Request Body
```json
{
  "username" : "Person",
  "password" :"TEST123",
  "confirmPassword":"TEST123"
}
```

#### Response

```json
{
"id": 1,
"username": "Christ"
}
```

## Spring Security Implementation 

### Overview

- Use of JWT : Login -> Get Token -> User submits another request with token -> Spring Security validates token -> token is valid -> User has access to requested resource

## Spring Security Jwt Flow Process

### 1. Security Config Class ("The Rulebook on how spring security behaves")

- Connects everything together
- Tells Spring Security which endpoints are public (like /login, /createUser)
- Tells Spring Security which endpoints require authentication
- Disables default session login (because JWT is stateless)
- Registers authentication filter
- Registers authorization filter
- Attaches UserDetailsService to authentication system

### 2. Login Step (Authentication Filter)
   
  - Extends `UsernamePasswordAuthenticationFilter`
  - Runs only when `/login` endpoint is hit
#### What is does (basically “Who are you? Prove it. OK here’s your token.”)

- Reads username + password from the login request
- Checks if credentials are correct
- If valid → creates a JWT token
- Sends JWT back to the client (usually in Authorization header)

### 3. Every Request Step (Authorization Filter)

- Extends BasicAuthenticationFilter
- Runs on EVERY request after login

#### What it does (basically “Is this request allowed? OK here’s your token.”)

- Reads JWT from Authorization header
- Verifies JWT is valid
- If valid → allows request to proceed
- If invalid → denies request

### 4. Look up User (UserDetailsService)

#### What it does (basically loads user from db from using username)

- Loads user from database using username
- Returns a Spring Security UserDetails object which contains
  - username
  - hashed passwords
  - authorities
- this matters since Spring Security needs to compare the password against the stored hashed password



## Learning Outcomes
- Built RESTful APIs using Spring Boot
- Implemented secure authentication and authorization using JWT
- Applied CI/CD practices using Jenkins pipelines
- Deployed applications on AWS EC2 instances
- Created and managed Docker containers (Tomcat and Jenkins)
- Created a Splunk dashboard and configured alerts for an EC2 instance.
- Gained hands-on experience with containerized deployment workflows

## Acknowledgements
This project was built as part of the Java Web Developer Nanodegree program from Udacity.  
Special thanks to Udacity for providing structured learning and hands-on backend development experience.
