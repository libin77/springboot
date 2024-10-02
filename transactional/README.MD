# Transactional Demo Application

This project is a Spring Boot application that demonstrates the use of transactional operations with H2 in-memory database, JPA, and Hibernate. It includes a simple model for `ProductList` and pre-populates some data into the database.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [H2 Database Console](#h2-database-console)
- [Project Structure](#project-structure)
- [License](#license)

## Features

- Spring Boot with JPA and Hibernate ORM
- In-memory H2 database
- Automatic database schema creation with `schema.sql`
- Data initialization with `data.sql`
- Transactional management of database operations

## Technologies

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Maven

## Setup

### Prerequisites

- Java 17 or higher
- Maven 3.x

### Clone the Repository

```bash
git clone https://github.com/your-username/transactional-demo.git
cd transactional-demo
```

### CONCEPTS

1. **Database transaction**
   * A database transaction is a unit of work that is executed as a single operation. 
   * It is a sequence of one or more SQL operations that are treated as a single logical unit.
   * A transaction must follow the ACID properties (Atomicity, Consistency, Isolation, Durability) to ensure data integrity.

2. **ACID properties**
   * Atomicity: Ensures that all operations within a transaction are completed; otherwise, none of them are applied.
   * Consistency: Ensures that the database moves from one valid state to another, maintaining database rules such as integrity constraints.
   * Isolation: Ensures that the intermediate state of a transaction is not visible to other transactions until the transaction is complete.
   * Durability: Guarantees that once a transaction has been committed, it will remain committed even in case of a system failure.

3. **How are transactions managed in Spring?**
   * Spring provides declarative and programmatic transaction management. 
   * The preferred way is to use declarative transaction management, which is done using annotations or XML configurations.
   * Declarative Transactions: Managed via @Transactional annotation or XML configuration.
   ```java
   @Transactional
   public void saveData() {
   // Transactional code
   }
   ```
    * Programmatic Transactions: Managed explicitly by calling transaction-related APIs, such as TransactionTemplate or PlatformTransactionManager.
    ```
        TransactionTemplate template = new TransactionTemplate(transactionManager);
         template.execute(status -> {
         // Transactional code
         return null;
         });
   ```
   
4. **Difference between declarative and programmatic transaction management in Spring**
   * Declarative Transaction Management: Achieved using annotations (@Transactional) or XML configuration. It separates the transaction management logic from business logic, making it easier to manage transactions. It is the most commonly used approach in Spring.
   * Programmatic Transaction Management: Requires explicit transaction control in code using the TransactionTemplate or PlatformTransactionManager. It offers fine-grained control over transaction management but is less convenient to use and can lead to verbose code.

5. **Propagation types in Spring transactions**
   * Transaction propagation defines how existing and new transactions interact. Spring supports the following propagation behaviors:
     * REQUIRED (default): Uses the current transaction if it exists, or creates a new one if none exists.
     * REQUIRES_NEW: Suspends the current transaction and creates a new one.
     * NESTED: If a transaction exists, a nested transaction is created within the current transaction.
     * SUPPORTS: Participates in a transaction if one exists; otherwise, executes non-transactionally.
     * bNOT_SUPPORTED: Suspends the current transaction and executes non-transactionally.
     * MANDATORY: Requires an existing transaction; throws an exception if no transaction exists.
     * NEVER: Executes non-transactionally and throws an exception if a transaction exists.

6. **Isolation levels in transactions, and how are they handled in Spring**
   * Isolation levels determine how transactions are isolated from one another. Different isolation levels prevent phenomena like dirty reads, non-repeatable reads, and phantom reads. Spring supports the following isolation levels:
   * {dirty reads - get wrong value if other transaction updated, non-repeatable reads - get different read results before and after when transaction update any value and phantom reads - get different read results before and after when transaction insert or delete any value}
     * READ_UNCOMMITTED: Allows dirty reads, non-repeatable reads, and phantom reads.
     * READ_COMMITTED: Prevents dirty reads but allows non-repeatable reads and phantom reads.
     * REPEATABLE_READ: Prevents dirty and non-repeatable reads but allows phantom reads.
     * SERIALIZABLE: Ensures full isolation by preventing dirty reads, non-repeatable reads, and phantom reads.
   * In Spring, isolation levels are configured using the @Transactional annotation:
   ```java
   @Transactional(isolation = Isolation.SERIALIZABLE)
   public void processTransaction() {
   // Transactional code
   }
   ```


7. **Default propagation and isolation level in Spring transactions**
   * Propagation: The default propagation level is Propagation.REQUIRED. This means that if a transaction already exists, the method will run within that transaction. If no transaction exists, a new one will be started.
   * Isolation: The default isolation level is usually the default of the underlying database, often READ_COMMITTED.

8. **What is the role of TransactionManager in Spring?**
   * The TransactionManager interface in Spring defines the methods needed to control transactions. 
   * Different implementations of this interface are used depending on the persistence technology (e.g., DataSourceTransactionManager for JDBC, JpaTransactionManager for JPA). 
   * Spring uses PlatformTransactionManager to abstract transaction management for different transaction APIs.

9. **Difference between rollbackFor and noRollbackFor in Spring's @Transactional**
   * rollbackFor: Specifies the types of exceptions for which the transaction should be rolled back. By default, Spring rolls back on unchecked exceptions (RuntimeException), but this can be customized.
   ```java
   @Transactional(rollbackFor = Exception.class)
   public void performAction() {
   // code
   }
   ```
   * noRollbackFor: Specifies the types of exceptions for which the transaction should not be rolled back, even if they occur.
    ``` java
    @Transactional(noRollbackFor = CustomException.class)
    public void performAction() {
    // code
    }
    ```

10. **What happens if you call a method annotated with @Transactional from another method in the same class?**
    * In Spring, @Transactional works by creating a proxy that wraps the transactional methods. 
    * If you call a method annotated with @Transactional from another method in the same class (i.e., self-invocation), the proxy will not be used, and the transactional behavior will not be applied. 
    * This is because Spring AOP proxies are only invoked from outside the class.

11. **What is the @Transactional(readOnly = true) annotation, and when would you use it?**
    * The @Transactional(readOnly = true) annotation is used to indicate that the transaction is read-only. 
    * This means that no data-modifying operations should be performed within the transaction, and it can help optimize performance by avoiding unnecessary locking or flushing in the persistence context.
    ```java
     @Transactional(readOnly = true)
     public List<Customer> getAllCustomers() {
     return customerRepository.findAll();
     }
    ```
    * It is commonly used for read operations to improve performance, especially when using JPA or Hibernate.

12. **How can you ensure that transactions are rolled back after a failure in Spring?**
    * Spring automatically rolls back a transaction if an unchecked exception (RuntimeException or its subclasses) is thrown. 
    * If you need to roll back the transaction on a checked exception, you can configure it explicitly with the rollbackFor attribute of the @Transactional annotation:
    ```java
    @Transactional(rollbackFor = Exception.class)
    public void performAction() throws Exception {
    // code
    }
    ```

13. **@EnableTransactionManagement in Spring?**
    * The @EnableTransactionManagement annotation is used to enable declarative transaction management in a Spring application. It is typically placed on a configuration class, allowing Spring to automatically scan and manage @Transactional annotations.
    ``` java
     @Configuration
     @EnableTransactionManagement
     public class AppConfig {
     // Other configuration
     }
    ```

14. **Difference between TransactionTemplate and @Transactional**
    * @Transactional: Is declarative and applies transaction management through AOP proxies. It abstracts transaction boundaries from the developer and is easier to use for most cases.
    * TransactionTemplate: Is a programmatic way of managing transactions, giving the developer fine-grained control over transaction boundaries. It is used when more complex transaction control is needed.

15. **What are phantom reads, and how can you prevent them?**
    * A phantom read occurs when a transaction reads a set of rows that match a condition, and another transaction inserts or deletes rows that would affect the result set of the first transaction’s query. 
    * This leads to inconsistent query results during the same transaction. 
    * To prevent phantom reads, you can use the SERIALIZABLE isolation level, which ensures full isolation by acquiring more restrictive locks.
