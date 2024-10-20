## Spring Cloud Gateway
<p>In an architecture with multiple clients and microservices, a typical
setup would involve an API Gateway that acts as the entry point for all
client requests. The API Gateway communicates with different
microservices behind it, handling routing, security, rate-limiting, and
aggregation as needed.</p>

### Key Components of the Architecture:
1. Clients: Various types of clients (e.g., Web, Mobile, IoT devices) interact with your system.
2. API Gateway: A centralized entry point that manages client requests and routes them to the appropriate microservices.
3. Microservices: Individual services that handle specific business logic (e.g., user service, order service, payment service). Each microservice can be independently deployed and scaled.
4. Service Discovery (optional): Dynamically locates microservices in the architecture (e.g., using Spring Cloud Netflix Eureka).
5. Database: Each microservice often has its own database, adhering to the database per service pattern.
6. Load Balancer: Distributes incoming traffic evenly across multiple instances of the API Gateway and microservices.
7. Security Layer: Manages centralized authentication and authorization (e.g., JWT, OAuth2, Keycloak).
8. Logging and Monitoring: Tracks performance and logs all requests passing through the system.
9. Resilience Patterns: Circuit breakers, retries, and fallbacks ensure availability even if some microservices fail.
```
    +------------------+       +-----------------+         +-----------------+
    |  Web Clients      |       | Mobile Clients  |         | Other Clients   |
    +------------------+       +-----------------+         +-----------------+
             |                         |                            |
             +-------------------------+----------------------------+
                                       |
                             +--------------------------+
                             |      API Gateway         |
                             | (Spring Cloud Gateway)   |
                             +--------------------------+
                                       |
        +-----------------+-----------+-----------+-------------------+
        |                 |                       |                   |
+----------------+   +----------------+   +----------------+   +----------------+
|   User Service |   |  Order Service  |   | Payment Service |   |  Product Service|
+----------------+   +----------------+   +----------------+   +----------------+
        |                 |                       |                   |
+----------------+   +----------------+   +----------------+   +----------------+
| User Database  |   | Order Database  |   | Payment DB     |   | Product DB     |
+----------------+   +----------------+   +----------------+   +----------------+


```

### Key Components Explanation:
1. Clients:
   Web Client: Could be a browser-based front-end application, like React, Angular, or Vue.js.
   Mobile Client: Android or iOS applications that communicate with the backend through the API Gateway.
   Other Clients: Could be IoT devices, third-party services, or desktop applications, each using the API Gateway to interact with your backend services.
2. API Gateway (Spring Cloud Gateway):
   Routing: Directs traffic to the appropriate microservice based on URL patterns, headers, or request parameters.
   Security: Centralizes authentication (e.g., OAuth2, JWT) and authorization for incoming requests.
   Aggregation: Combines results from multiple services into a single response (especially useful for mobile clients).
   Rate Limiting: Ensures that no single client can overwhelm the services.
   Circuit Breaker: Protects services from being overloaded by failed requests.
   Load Balancing: Distributes traffic across multiple instances of microservices.
3. Microservices:
   User Service: Handles user-related logic such as registration, login, profile management, etc.
   Order Service: Manages the order lifecycle, including placing, updating, and tracking orders.
   Payment Service: Handles payments, billing, and financial transactions.
   Product Service: Manages product catalog, inventory, and details. 
    <p><b>Each microservice:</b>
    Is deployed independently, typically in containers (e.g., Docker).
    Communicates via HTTP REST APIs (or sometimes messaging like RabbitMQ or Kafka).
    Owns its data (using the database-per-service pattern).</p>

4. Service Discovery (Optional):
   Allows services to register themselves so that the API Gateway or other services can discover them dynamically (e.g., Spring Cloud Netflix Eureka).
   Useful for auto-scaling services where the number of service instances can change dynamically.
5. Security Layer:
   OAuth2 / JWT: The API Gateway handles token validation, ensuring only authenticated and authorized users can access certain routes.
   Keycloak or similar IAM tools: Used for centralized user authentication, role-based access control (RBAC), and single sign-on (SSO).
6. Load Balancer:
   Ensures even distribution of traffic across multiple instances of the API Gateway and microservices.
   Cloud providers like AWS, GCP, or Azure typically offer built-in load balancing.
   Spring Cloud Gateway can use Spring Cloud LoadBalancer to distribute traffic between instances.
7. Databases (Database-per-service):
   Each microservice typically has its own database to enforce data isolation and enable independent scaling of services.
   Databases could vary by service (e.g., relational databases like MySQL/PostgreSQL for the User Service, NoSQL databases like MongoDB for the Product Service).
8. Logging and Monitoring:
   Centralized logging: Tools like ELK Stack (Elasticsearch, Logstash, Kibana) or Prometheus + Grafana to monitor the health and performance of microservices.
   Distributed tracing: Tools like Zipkin or Jaeger to track requests across multiple services in the system.
9. Resilience Patterns:
   Circuit Breaker: Using tools like Resilience4j to prevent cascading failures by breaking the connection between services if one service starts failing.
   Retries and Fallbacks: API Gateway can implement retry logic when a service fails temporarily and provide fallback responses.

### Flow Example:
1. Client Request to API Gateway: A client sends a request (e.g., fetch user details) to the API Gateway.
2. Routing and Authentication: The API Gateway validates the client's token (e.g., JWT) and authenticates the user.
It determines that the request needs to be forwarded to the User Service.
3. Communication with Microservices: The API Gateway forwards the request to the User Service.
The User Service retrieves the user details from its database and sends the response back.
4. Aggregation (Optional): If a single request requires data from multiple services (e.g., order details + user details), the API Gateway can aggregate the responses and return a single unified response to the client.
5. Response to the Client: The API Gateway forwards the final response back to the client.

### Benefits of This Architecture:
1. Scalability: Microservices can be scaled independently based on the load.
2. Resilience: Failures in one microservice don't affect the others, and circuit breakers ensure the system remains operational.
3. Flexibility: Services can be updated, replaced, or scaled without affecting other parts of the system.
4. Security: Centralized security management in the API Gateway prevents unauthorized access to services.
5. Easier Client Management: Clients only need to know about the API Gateway and not the specific microservice URLs or implementations.

## Rest Template vs Web Client

|Spring WebClient   | Spring RestTemplate|
| -------- | - |
| In Spring WebClient,An HTTP request client is included in Spring WebFlux. | In Spring RestTemplate,REST APIs are becoming more and more common because of their heavy traffic and fast service accessibility. |
| Spring WebClient supports reactive spring and is based on event driven concepts.| Spring RestTemplate is synchronous and it’s reactive situations cannot use it. |
| Spring WebClient is asynchronous, it won’t stop the running thread while it waits for a response.  | Spring RestTemplate is synchronous and blocking since it makes use of the Java Servlet API. |
| Spring WebClient requires Java 8 or higher.   | Spring RestTemplate works with Java 6 and later versions. |
| Spring WebClient is a versatile library for facilitating communication.   | Spring RestTemplate is an advanced abstraction. |
| Microservices, reactive apps, and situations needing a high level of concurrency are the greatest uses for WebClient. | Perfect for straightforward use cases and conventional monolithic apps in RestTemplate |
| Spring WebClient probably going to continue growing and getting assistance in springtime. | Spring RestTemplate receives security updates but does not receive any new features in future. |