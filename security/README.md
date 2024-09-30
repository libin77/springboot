# Spring Security Demo
#### Spring Boot’s inbuilt H2 database, trying out different security mechanisms such as basic authentication, JWT, and OAuth2.

## 1. Set Up Spring Boot Project with H2 Database
   * Use Spring Initializr.
   * Dependencies: Select:
     * Spring Web
     * Spring Security
     * Spring Data JPA
     * H2 Database
     * (Optional) Spring Boot Starter OAuth2 (for OAuth2)
   * Download and extract the project.
   
## 2. Configure H2 Database
   * In src/main/resources/application.properties, configure H2:
   properties
  ```
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=password
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   spring.jpa.hibernate.ddl-auto=update
   ```
   * Now, you can access the H2 console at http://localhost:8080/h2-console with the JDBC URL as jdbc:h2:mem:testdb.
   
## 3. Create JPA Entity for User
   * Create a User entity class that will represent users in the database.
```
import javax.persistence.*;

  @Entity
  @Table(name = "users")
  public class User {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      private String username;
      private String password;
      private String role;

      // Getters and setters omitted for brevity
  }
```

## 4. Create UserRepository
   * Define a UserRepository to manage user records in the H2 database.
   ```
   import org.springframework.data.jpa.repository.JpaRepository;
   import java.util.Optional;

   public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByUsername(String username);
   }
  ```

## 5. Configure Basic Authentication
   * In your SecurityConfig class, use the UserDetailsService to load users from the database.
   ```
   import org.springframework.context.annotation.Bean;
   import org.springframework.security.config.annotation.web.builders.HttpSecurity;
   import org.springframework.security.core.userdetails.UserDetails;
   import org.springframework.security.core.userdetails.UserDetailsService;
   import org.springframework.security.core.userdetails.UsernameNotFoundException;
   import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
   import org.springframework.security.crypto.password.PasswordEncoder;
   import org.springframework.stereotype.Service;

    @Service
    public class MyUserDetailsService implements UserDetailsService {

      private final UserRepository userRepository;

      public MyUserDetailsService(UserRepository userRepository) {
          this.userRepository = userRepository;
      }

      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
          return org.springframework.security.core.userdetails.User
                  .withUsername(user.getUsername())
                  .password(user.getPassword())
                  .roles(user.getRole())
                  .build();
        }
      }

      @EnableWebSecurity
      public class SecurityConfig {

        private final MyUserDetailsService userDetailsService;

        public SecurityConfig(MyUserDetailsService userDetailsService) {
          this.userDetailsService = userDetailsService;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic();  // Basic authentication
            return http.build();
        }
    }
  ```

## 6. Test Basic Authentication
   Create sample users and save them to the H2 database using a CommandLineRunner.
   ```
   import org.springframework.boot.CommandLineRunner;
   import org.springframework.context.annotation.Bean;
   import org.springframework.security.crypto.password.PasswordEncoder;

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder       passwordEncoder) {
    return args -> {
    userRepository.save(new User(null, "user", passwordEncoder.encode("password"), "USER"));
    userRepository.save(new User(null, "admin", passwordEncoder.encode("admin"), "ADMIN"));
    };
    }
   ```
  * Now, you can authenticate with Basic Auth using these credentials.
    

## 7. Implement JWT Authentication
   JWT (JSON Web Token) adds token-based authentication. Here’s how to implement it:

* Add jjwt dependency to pom.xml:

```
<dependency>
<groupId>io.jsonwebtoken</groupId>
<artifactId>jjwt-api</artifactId>
<version>0.11.2</version>
</dependency>
<dependency>
<groupId>io.jsonwebtoken</groupId>
<artifactId>jjwt-impl</artifactId>
<version>0.11.2</version>
</dependency>
<dependency>
<groupId>io.jsonwebtoken</groupId>
<artifactId>jjwt-jackson</artifactId>
<version>0.11.2</version>
</dependency>
```

* Create a JwtUtil class to handle token generation and validation.

```
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private String SECRET_KEY = "secret";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
```

Implement the JWT security filters and authentication, replacing Basic Auth. You can now authenticate users and secure endpoints using JWT.

## 8. Implement OAuth2 with GitHub
   Spring Security provides OAuth2 out of the box. Add the following to application.properties:

```
spring.security.oauth2.client.registration.github.client-id=<your-client-id>
spring.security.oauth2.client.registration.github.client-secret=<your-client-secret>
spring.security.oauth2.client.registration.github.scope=read:user
spring.security.oauth2.client.registration.github.redirect-uri={baseUrl}/login/oauth2/code/github
```

Configure OAuth2 login in SecurityConfig:

```
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http
.authorizeRequests()
.anyRequest().authenticated()
.and()
.oauth2Login();  // OAuth2 login with GitHub
return http.build();
}
```
Now you can test OAuth2 login using GitHub!

## 9. Push Code to GitHub

   After testing all security implementations, push your code to GitHub:
   ```
   git add .
   git commit -m "Added Basic Auth, JWT, and OAuth2 Security"
   git push
   ```
