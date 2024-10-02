# Spring Boot JWT Security with H2 Database

This project demonstrates how to implement JWT (JSON Web Token) based authentication with Spring Boot and store sample users in an H2 in-memory database.

## Features

- JWT-based Authentication
- Role-based Authorization
- In-memory H2 Database
- Spring Security Configuration
- H2 Console Access
- REST API endpoints for authentication and user management

## Technologies Used

- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- H2 Database (In-memory)
- Postman for API testing

## Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.x
- Postman (optional for API testing)

### Installation and Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/your-repo/spring-boot-jwt-example.git
    cd spring-boot-jwt-example
    ```

2. Build the project using Maven:

    ```bash
    mvn clean install
    ```

3. Run the Spring Boot application:

    ```bash
    mvn spring-boot:run
    ```

4. Access the H2 database console at:

    ```
    http://localhost:8080/h2-console
    ```

   **JDBC URL**: `jdbc:h2:mem:testdb`  
   **Username**: `sa`  
   **Password**: (leave blank)

### API Endpoints

The following endpoints are available for testing:

- `POST /register`: Register a new user
- `POST /authenticate`: Authenticate a user and get a JWT token
- `GET /users`: Get all users (Requires a valid JWT token)
- `GET /h2-console`: Access the H2 console

### API Testing with Postman

1. **Import Postman Collection**

   Use the Postman collection file along with repo.

   **Steps to Import:**
    - Open Postman
    - Click on "Import" in the top-left corner
    - Select the `spring-boot-jwt-example.postman_collection.json` file
    - Click "Import"

2. **Register a User**

   Send a `POST` request to `/register` with the following JSON body to create a new user:

   ```json
   {
       "username": "testuser",
       "password": "password123",
       "role": "USER"
   }
   
3. **Authenticate a User**

    Send a POST request to /authenticate with the following JSON body to authenticate the user and receive a JWT token:

    ```json
    {
    "username": "testuser",
    "password": "password123"
    }
   ```
    You will receive a response like this:

    ```json
    {
    "token": "your-jwt-token-here"
    }
   ```
4. **Access Protected Endpoints**

    Use the JWT token in the Authorization header with the Bearer prefix to access protected endpoints. For example:

    * GET /users: Fetch all users (requires authentication).
   Get the Authorization header like this:
    ```
    Authorization: Bearer your-jwt-token-here
    ```

5. **Testing Role-Based Access Control**

   Now, you can test role-based authorization with Postman using the following endpoints:

   * Admin endpoints (accessible only to users with ROLE_ADMIN):
     * GET /admin/info
     * GET /admin/dashboard
   * User endpoints (accessible for both users with ROLE_USER and ROLE_ADMIN):
     * GET /user/info
     * GET /user/dashboard

## H2 Database
To view the users or other data in the H2 database:

Go to the H2 console: http://localhost:8080/h2-console
Use the following settings:
* JDBC URL: jdbc:h2:mem:testdb
* Username: sa
* Password: (leave blank)
Run SQL queries to view or manipulate the data.
For example, you can run:

```sql
SELECT * FROM ACTOR;
```



 