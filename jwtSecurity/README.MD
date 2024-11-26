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

## CONCEPTS

1. **Spring Security**
   * Spring Security is a framework that provides authentication, authorization, and protection against common attacks (like CSRF) in Java applications. 
   * It integrates with the Spring Framework through the use of annotations, filters, and security contexts, allowing security concerns to be managed separately from business logic. 
   * Spring Security integrates seamlessly with Spring MVC, Spring Boot, and other Spring components via auto-configuration.

2. **Architecture of Spring Security**
   * Spring Security's architecture primarily consists of several core components:
     * AuthenticationManager: The entry point for authentication.
     * AuthenticationProvider: Used to process authentication requests.
     * UserDetailsService: Interface used to retrieve user-related data, typically used by AuthenticationProvider.
     * GrantedAuthority: Represents roles or privileges.
     * SecurityContext: Holds the authentication details for a user.
     * Filters: Spring Security uses a chain of filters (like UsernamePasswordAuthenticationFilter) to process security-related tasks.

3. **Configure Spring Security in a Spring Boot application**
   * In Spring Boot, Spring Security can be configured using SecurityConfig by extending WebSecurityConfigurerAdapter and overriding key methods:
    ```java
   //New version can be referred in this repo no need to extends adaptor or override methods 
   @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    http
    .authorizeRequests()
    .antMatchers("/admin/**").hasRole("ADMIN")
    .antMatchers("/user/**").hasRole("USER")
    .antMatchers("/", "/public").permitAll()
    .and()
    .formLogin()
    .and()
    .logout();
    }
    }
   ```
   * Spring Boot also provides some default security configurations out of the box if no explicit configuration is defined.

4. **Difference between @PreAuthorize and @Secured**
   * @PreAuthorize: It is part of Spring EL (SpEL) and allows for more complex expression-based access control. It can evaluate roles, permissions, and other conditions.
   ```
   @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
    ```

    * @Secured: It is a simpler annotation that checks for specific roles and cannot handle more complex expressions.
   ```
    @Secured("ROLE_ADMIN")
   ```

5. **Role of SecurityContextHolder**
   * The SecurityContextHolder holds the SecurityContext containing the authentication and security information for the current execution thread. 
   * It can be accessed statically from anywhere in the application, which makes it useful for checking the current user’s security details.

6. **Spring Security handle CSRF (Cross-Site Request Forgery)**
   * Spring Security provides built-in support for CSRF protection by generating a unique token for each session or request. 
   * This token is then required to be included in the request, preventing malicious actors from forging requests on behalf of authenticated users. 
   * By default, Spring Security enables CSRF protection in form-based authentication:
   ```java
   @Override
   protected void configure(HttpSecurity http) throws Exception {
   http
   .csrf().enable();
   }
   ```

   * You can also disable it for certain endpoints (e.g., APIs):
    ```
    http
   .csrf().disable();
   ```

7. **Different types of authentication mechanisms supported**
   * Spring Security supports a variety of authentication mechanisms:
     * Form-based Authentication: Using an HTML form to gather username and password.
     * Basic Authentication: Sends credentials via HTTP headers (username and password encoded in base64).
     * Digest Authentication: Similar to Basic but more secure (password hashing and nonces).
     * OAuth2: Token-based authentication for APIs.
     * JWT Authentication: JSON Web Tokens used to manage stateless sessions.
     * LDAP: Authentication via LDAP protocol.
     * SAML: Used for single sign-on (SSO) with external providers.

8. **Spring Security handle method-level security**
   * Method-level security in Spring Security can be enabled with annotations like @PreAuthorize, @PostAuthorize, @Secured, and @RolesAllowed. It provides fine-grained access control over service methods. Method-level security can be enabled using @EnableGlobalMethodSecurity annotation:
   ```java
   @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
   public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
   }
   ```


9. **Role of GrantedAuthority**
   * GrantedAuthority is used to represent the authorities (or roles) granted to a user. 
   * It is an interface that typically contains the user’s roles or permissions. 
   * Each authenticated user will have a collection of GrantedAuthority objects, which Spring Security uses to determine if they have the appropriate permissions to access a resource.

10. **How does Spring Security handle stateless authentication?**
    * Stateless authentication is typically implemented using JWT (JSON Web Tokens). 
    * Instead of maintaining a session, the server issues a token (JWT) after successful authentication. 
    * The token is then passed along with each request in the Authorization header. 
    * Spring Security can validate the token and extract the user details from it without storing session data.

11. **What are filters in Spring Security, and how do they work?**
    * Filters in Spring Security are part of the Servlet Filter chain and are used to process incoming requests before they reach the controller. 
    * They handle authentication, authorization, CSRF protection, etc. Key filters include:
      * UsernamePasswordAuthenticationFilter: Processes login requests.
      * BasicAuthenticationFilter: Processes HTTP Basic authentication.
      * JwtAuthenticationFilter: Custom filters for handling JWT tokens.

12. **What is OAuth2 and how is it integrated with Spring Security**
    * OAuth2 is an authorization framework that enables third-party services to exchange tokens for granting access to protected resources. 
    * Spring Security provides support for OAuth2 through Spring Security OAuth and Spring Security OAuth2 Client. 
    * Spring Boot makes it easier to configure OAuth2 login with minimal configuration.
    ```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    http
    .oauth2Login();
    }
    ```


13. **Difference between hasRole and hasAuthority**
    * In Spring Security, hasRole assumes that roles are prefixed with ROLE_. For example, hasRole('ADMIN') expects a GrantedAuthority of ROLE_ADMIN. 
    * hasAuthority, on the other hand, does not apply any prefix and checks for an exact match of the authority. 
    * So hasAuthority('ADMIN') looks for an exact match without any prefix.


ABOUT JWT
ENCODED: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
DECODED: 
HEADER:ALGORITHM & TOKEN TYPE

{
  "alg": "HS256",
  "typ": "JWT"
}

PAYLOAD:DATA

{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022
}

VERIFY SIGNATURE

HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  
your-256-bit-secret

) secret base64 encoded




 
