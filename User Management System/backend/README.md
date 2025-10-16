# User Management API - Backend

A robust Spring Boot REST API for user management with H2 in-memory database, Hibernate Search integration, and comprehensive data loading capabilities from external APIs.

## 🎯 Overview

This backend service provides:
- RESTful API endpoints for user management
- Full-text search capabilities with Hibernate Search
- Resilient data loading from DummyJSON API
- Comprehensive error handling and validation
- Swagger/OpenAPI documentation
- High test coverage with JUnit and Mockito

## 🛠 Technology Stack

- **Java 17** - Programming Language
- **Spring Boot 3.2.0** - Application Framework
- **Spring Data JPA** - Data Persistence
- **H2 Database** - In-Memory Database
- **Hibernate Search 7.0** - Full-Text Search Engine
- **Swagger/OpenAPI** - API Documentation
- **Spring Retry** - Resilience Patterns
- **JUnit 5** - Testing Framework
- **Mockito** - Mocking Framework
- **JaCoCo** - Code Coverage Analysis

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone and navigate to backend directory:**
   ```bash
   git clone <repository-url>
   cd backend
   ```

2. **Run with Maven:**
   ```bash
   mvn spring-boot:run
   ```

3. **Alternative - Run JAR file:**
   ```bash
   mvn clean package
   java -jar target/user-management-api-0.0.1-SNAPSHOT.jar
   ```

### Application URLs
- **API Base URL:** http://localhost:8084/api
- **Swagger UI:** http://localhost:8084/swagger-ui.html
- **H2 Console:** http://localhost:8084/h2-console
- **OpenAPI Docs:** http://localhost:8084/api-docs

## 📋 API Documentation

### User Management Endpoints

#### Get All Users
```http
GET /api/users
```
Returns a list of all users in the system.

**Response Example:**
```json
[
  {
    "id": 1,
    "firstName": "Emily",
    "lastName": "Johnson",
    "ssn": "900-590-289",
    "email": "emily.johnson@x.dummyjson.com",
    "age": 28,
    "role": "admin",
    "phone": "+81 965-431-3024",
    "username": "emilys",
    "birthDate": "1996-5-30",
    "gender": "female"
  }
]
```

#### Get User by ID
```http
GET /api/users/{id}
```
Retrieves a specific user by their unique ID.

**Parameters:**
- `id` (path) - User ID (required)

#### Get User by Email
```http
GET /api/users/email/{email}
```
Retrieves a user by their email address.

**Parameters:**
- `email` (path) - User email address (required)

#### Search Users
```http
GET /api/users/search?q={searchTerm}
```
Search users by firstName, lastName, or SSN with full-text search capabilities.

**Parameters:**
- `q` (query) - Search term (optional)
- For 3+ characters: Uses Hibernate Search with Lucene
- For 1-2 characters: Uses basic JPA repository search
- Empty query returns all users

### Data Management Endpoints

#### Load Users from External API
```http
POST /api/data/load
```
Loads user data from the DummyJSON external API into the local H2 database.

**Response Example:**
```json
{
  "success": true,
  "message": "Users data loaded successfully",
  "previousCount": 0,
  "currentCount": 30,
  "loadedCount": 30
}
```

#### Get Data Status
```http
GET /api/data/status
```
Returns the current status of loaded data.

**Response Example:**
```json
{
  "totalUsers": 30,
  "dataLoaded": true
}
```

## 🗂 Project Structure

```
src/main/java/com/example/usermanagement/
├── controller/              # REST Controllers
│   ├── UserController.java      # User management endpoints
│   └── DataLoadController.java  # Data loading endpoints
├── service/                 # Business Logic Layer
│   ├── UserService.java         # User business operations
│   └── DataLoadService.java     # External API integration
├── repository/              # Data Access Layer
│   ├── UserRepository.java      # JPA Repository
│   └── UserSearchRepository.java # Hibernate Search Repository
├── entity/                  # JPA Entities
│   └── User.java                # User entity
├── dto/                     # Data Transfer Objects
│   ├── UserResponseDto.java     # User response DTO
│   ├── DummyJsonUserDto.java    # External API DTO
│   └── DummyJsonResponseDto.java # External API response DTO
├── config/                  # Configuration Classes
│   ├── RestTemplateConfig.java  # HTTP client config
│   ├── OpenApiConfig.java       # Swagger configuration
│   ├── WebConfig.java           # Web/CORS configuration
│   └── DataInitializer.java     # Startup data loading
├── exception/               # Exception Handling
│   ├── GlobalExceptionHandler.java # Global exception handler
│   ├── UserNotFoundException.java  # Custom exceptions
│   └── DataLoadException.java
└── UserManagementApplication.java # Main application class
```

## 🔧 Configuration

### Application Properties (application.yml)

```yaml
spring:
  application:
    name: user-management-api
  
  # Database Configuration
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  
  # H2 Console Configuration
  h2:
    console:
      enabled: true
      path: /h2-console
  
  # JPA Configuration
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        search:
          backend:
            type: lucene
            directory:
              type: local-heap

server:
  port: 8084

# External API Configuration
api:
  external:
    dummyjson:
      base-url: https://dummyjson.com
      retry:
        max-attempts: 3
        delay: 1000

# Swagger Configuration
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
```

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests in specific package
mvn test -Dtest=com.example.usermanagement.service.*
```

### Test Coverage
The project maintains high test coverage with comprehensive unit tests:

- **UserServiceTest** - Tests business logic layer
- **UserControllerTest** - Tests REST API endpoints
- **DataLoadServiceTest** - Tests external API integration
- **Integration Tests** - End-to-end testing scenarios

Coverage reports are generated in: `target/site/jacoco/index.html`

### Test Structure
```
src/test/java/com/example/usermanagement/
├── controller/
│   └── UserControllerTest.java
├── service/
│   ├── UserServiceTest.java
│   └── DataLoadServiceTest.java
└── integration/
    └── UserManagementIntegrationTest.java
```

## 🔒 Security Features

- **Input Validation** - Bean validation with custom annotations
- **SQL Injection Prevention** - JPA parameterized queries
- **CORS Configuration** - Configured for frontend integration
- **Error Handling** - Sanitized error responses
- **Logging** - Comprehensive application logging

## 🔄 Resilience Patterns

### Retry Mechanism
The application implements retry patterns for external API calls:

```java
@Retryable(
  retryFor = {Exception.class},
  maxAttemptsExpression = "${api.external.dummyjson.retry.max-attempts}",
  backoff = @Backoff(delayExpression = "${api.external.dummyjson.retry.delay}")
)
public void loadUsersFromExternalAPI() {
  // Implementation with retry logic
}
```

### Error Handling
Global exception handling with proper HTTP status codes:

- `404 Not Found` - User not found
- `400 Bad Request` - Validation errors
- `500 Internal Server Error` - System errors

## 📊 Performance Optimizations

- **Connection Pooling** - HikariCP for database connections
- **Lazy Loading** - JPA lazy loading for relationships
- **Indexing** - Lucene full-text search indexing
- **Caching** - Ready for Redis integration
- **Pagination** - Support for large datasets

## 🔍 Search Implementation

### Hibernate Search Configuration
```java
@Entity
@Indexed
public class User {
    @FullTextField(analyzer = "standard")
    private String firstName;
    
    @FullTextField(analyzer = "standard")
    private String lastName;
    
    @FullTextField(analyzer = "standard")
    private String ssn;
}
```

### Search Repository
```java
public List<User> fullTextSearch(String searchTerm) {
    SearchSession searchSession = Search.session(entityManager);
    
    return searchSession.search(User.class)
            .where(f -> f.match()
                    .fields("firstName", "lastName", "ssn")
                    .matching(searchTerm + "*"))
            .fetchAllHits();
}
```

## 🚀 Production Considerations

### Building for Production
```bash
# Create production build
mvn clean package -Pprod

# Run with production profile
java -jar -Dspring.profiles.active=prod target/user-management-api-0.0.1-SNAPSHOT.jar
```

### Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/userdb
export SPRING_DATASOURCE_USERNAME=username
export SPRING_DATASOURCE_PASSWORD=password
export API_EXTERNAL_DUMMYJSON_BASE_URL=https://dummyjson.com
```

### Docker Support
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/user-management-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🔧 Monitoring and Logging

### Logging Configuration
```yaml
logging:
  level:
    com.example.usermanagement: DEBUG
    org.springframework.web: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/application.log
```

### Health Checks
Spring Boot Actuator endpoints (can be enabled):
- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application info

## 🚧 Future Enhancements

- [ ] Database migration to PostgreSQL/MySQL
- [ ] Authentication and authorization (Spring Security)
- [ ] Rate limiting for API endpoints
- [ ] Caching with Redis
- [ ] Message queues for async processing
- [ ] Monitoring with Prometheus and Grafana
- [ ] Advanced search filters and sorting
- [ ] API versioning strategy
- [ ] Containerization with Docker
- [ ] CI/CD pipeline setup

## 📞 Support

For technical support:
1. Check the Swagger documentation at `/swagger-ui.html`
2. Review the application logs
3. Use H2 console at `/h2-console` for database inspection
4. Create issues in the project repository

---

**Spring Boot User Management API** - Built with ❤️