# User Management System

A comprehensive full-stack application featuring a Spring Boot backend with H2 in-memory database and Hibernate Search integration, paired with a React TypeScript frontend for user management with advanced search, filtering, and sorting capabilities.

## 🚀 Features

### Backend Features
- **RESTful API** with comprehensive Swagger/OpenAPI documentation
- **H2 In-Memory Database** for fast development and testing
- **Hibernate Search** with Lucene for advanced full-text search
- **Resilient Data Loading** with retry mechanisms and error handling
- **Comprehensive Validation** and exception handling
- **External API Integration** with DummyJSON for data loading
- **High Test Coverage** with unit and integration tests
- **Clean Architecture** with proper separation of concerns

### Frontend Features
- **Modern React UI** with TypeScript and Material-UI
- **Real-time Search** with debouncing and autocomplete
- **Client-side Sorting** by age, name (ascending/descending)
- **Advanced Filtering** by user roles
- **Responsive Design** that works on all device sizes
- **Loading States** and error handling with user feedback
- **Lazy Loading** techniques for optimal performance
- **Single Page Application** with smooth user experience

## 🏗 Architecture

```
vicky-ps/
├── backend/                 # Spring Boot Application
│   ├── src/main/java/
│   │   └── com/example/usermanagement/
│   │       ├── controller/  # REST Controllers
│   │       ├── service/     # Business Logic
│   │       ├── repository/  # Data Access Layer
│   │       ├── entity/      # JPA Entities
│   │       ├── dto/         # Data Transfer Objects
│   │       ├── config/      # Configuration Classes
│   │       └── exception/   # Exception Handling
│   ├── src/test/           # Unit Tests
│   └── pom.xml             # Maven Dependencies
└── frontend/               # React TypeScript Application
    ├── src/
    │   ├── components/     # React Components
    │   ├── services/       # API Services
    │   ├── types/          # TypeScript Types
    │   ├── utils/          # Utility Functions
    │   └── __tests__/      # Unit Tests
    └── package.json        # NPM Dependencies
```

## 🛠 Technology Stack

### Backend
- **Java 17** - Programming Language
- **Spring Boot 3.2.0** - Application Framework
- **Spring Data JPA** - Data Access
- **H2 Database** - In-Memory Database
- **Hibernate Search 7.0** - Full-Text Search
- **Swagger/OpenAPI** - API Documentation
- **Spring Retry** - Resilience Patterns
- **JUnit 5** - Testing Framework
- **Mockito** - Mocking Framework
- **JaCoCo** - Code Coverage

### Frontend
- **React 18** - UI Library
- **TypeScript** - Type Safety
- **Material-UI (MUI)** - Component Library
- **Axios** - HTTP Client
- **React Testing Library** - Testing
- **Jest** - Testing Framework

## 🚦 Quick Start

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Maven 3.6 or higher
- NPM or Yarn

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access API Documentation:**
   - Swagger UI: http://localhost:8084/swagger-ui.html
   - H2 Console: http://localhost:8084/h2-console
   - API Docs: http://localhost:8084/api-docs

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start development server:**
   ```bash
   npm start
   ```

4. **Access the application:**
   - Frontend: http://localhost:3000

## 📋 API Endpoints

### User Management
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `GET /api/users/search?q={searchTerm}` - Search users by name, lastName, or SSN

### Data Management
- `POST /api/data/load` - Load users from external DummyJSON API
- `GET /api/data/status` - Get data loading status

## 🔍 Search Capabilities

The application supports multiple search modes:

1. **Full-Text Search (3+ characters):**
   - Uses Hibernate Search with Lucene indexing
   - Searches across firstName, lastName, and SSN fields
   - Supports wildcard matching and partial terms

2. **Basic Search (1-2 characters):**
   - Uses JPA repository queries
   - Case-insensitive LIKE queries
   - Immediate results for short terms

3. **Advanced Filtering:**
   - Client-side role-based filtering
   - Dynamic filter combinations
   - Real-time result updates

## 🧪 Testing

### Backend Testing
```bash
cd backend
mvn test                    # Run unit tests
mvn jacoco:report          # Generate coverage report
```

Coverage reports are available at: `target/site/jacoco/index.html`

### Frontend Testing
```bash
cd frontend
npm test                   # Run unit tests
npm run test:coverage     # Run tests with coverage
```

## 🔧 Configuration

### Backend Configuration
Key configuration properties in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: password
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        search:
          backend:
            type: lucene

api:
  external:
    dummyjson:
      base-url: https://dummyjson.com
      retry:
        max-attempts: 3
        delay: 1000
```

### Frontend Configuration
Environment variables in `.env`:

```env
REACT_APP_API_URL=http://localhost:8084/api
```

## 📊 Performance Features

### Backend
- **Connection Pooling** with HikariCP
- **Lazy Loading** for JPA entities
- **Retry Mechanisms** for external API calls
- **Indexed Search** with Lucene
- **Pagination Support** (ready for implementation)

### Frontend
- **Debounced Search** (300ms delay)
- **Memoized Components** for optimal re-renders
- **Lazy Loading** components
- **Client-side Caching** of search results
- **Responsive Design** with CSS-in-JS

## 🚀 Production Deployment

### Backend Deployment
1. Build the JAR file:
   ```bash
   mvn clean package
   ```

2. Run the application:
   ```bash
   java -jar target/user-management-api-0.0.1-SNAPSHOT.jar
   ```

### Frontend Deployment
1. Build the production bundle:
   ```bash
   npm run build
   ```

2. Deploy the `build/` directory to your web server.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -am 'Add feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request

## 📝 Development Guidelines

### Backend
- Follow **Clean Code** principles
- Use **SOLID** design patterns
- Write **comprehensive tests** (aim for 80%+ coverage)
- Add **proper logging** for debugging
- Handle **exceptions gracefully**
- **Validate all inputs**

### Frontend
- Follow **Atomic Design** principles
- Use **TypeScript** for type safety
- Write **unit tests** for components
- Implement **responsive design**
- Handle **loading states** and errors
- Use **semantic HTML** for accessibility

## 🎯 Future Enhancements

- [ ] User authentication and authorization
- [ ] Real database integration (PostgreSQL/MySQL)
- [ ] Caching with Redis
- [ ] Microservices architecture
- [ ] GraphQL API
- [ ] Advanced search filters
- [ ] User profile management
- [ ] Audit logging
- [ ] Performance monitoring
- [ ] Docker containerization

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the API documentation at http://localhost:8084/swagger-ui.html

---

**Built with ❤️ using Spring Boot and React**