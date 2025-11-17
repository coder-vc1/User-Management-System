cat > README.md << 'EOF'
# 📝 Mini Blogging Platform

A full-stack blogging platform with user authentication, CRUD operations, and an AI-powered support assistant. Built with **Java Spring Boot** backend and **Next.js** frontend.

![Mini Blog Platform](https://img.shields.io/badge/Status-Production%20Ready-success)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![Next.js](https://img.shields.io/badge/Next.js-14.0.4-black)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)

## ✨ Features

### 🔐 Authentication
- User registration with email validation
- Secure login with JWT tokens
- Password hashing with BCrypt
- Persistent authentication with localStorage
- Auto-logout on session expiration

### 📝 Blog Management
- **Create** - Write and publish blog posts with Markdown support
- **Read** - Browse all published blogs
- **Update** - Edit your own blogs
- **Delete** - Remove your own blogs
- Author-only edit/delete permissions
- Real-time content preview

### 🤖 AI Support Assistant
- Knowledge base powered Q&A system
- Keyword matching algorithm
- Instant responses to platform queries
- FAQ section with common questions

### 🎨 UI/UX
- Beautiful dark theme with gradient effects
- Animated border cards with pulse glow
- Fully responsive design (mobile-first)
- Smooth animations and transitions
- Toast notifications for user feedback
- Loading states and error handling

## 🛠️ Tech Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Database operations
- **MySQL 8.0** - Database
- **JWT (JSON Web Tokens)** - Secure authentication
- **Lombok** - Boilerplate code reduction
- **ModelMapper** - Entity-DTO mapping
- **SpringDoc OpenAPI** - API documentation
- **Maven** - Dependency management

### Frontend
- **Next.js 14** - React framework with App Router
- **React 18** - UI library
- **TypeScript** - Type-safe development
- **Zustand** - State management
- **Tailwind CSS** - Utility-first CSS
- **React Hook Form** - Form validation
- **Axios** - HTTP client
- **React Hot Toast** - Toast notifications
- **React Markdown** - Markdown rendering
- **date-fns** - Date formatting

## 📁 Project Structure
```
mini-blog-platform/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/blogging/
│   │   ├── config/            # Configuration classes
│   │   ├── controller/        # REST controllers
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── entity/           # JPA entities
│   │   ├── exception/        # Custom exceptions
│   │   ├── repository/       # Data repositories
│   │   ├── security/         # Security configuration
│   │   ├── service/          # Business logic
│   │   └── util/             # Utility classes
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
└── frontend/                   # Next.js frontend
    ├── app/                   # App router pages
    │   ├── auth/             # Authentication pages
    │   ├── blogs/            # Blog pages
    │   ├── support/          # AI support page
    │   ├── layout.tsx
    │   ├── page.tsx
    │   └── globals.css
    ├── components/            # React components
    ├── hooks/                # Custom hooks
    ├── store/                # Zustand stores
    ├── types/                # TypeScript types
    └── package.json
```

## 🚀 Getting Started

### Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Node.js 18+**
- **npm or yarn**

### Backend Setup

1. **Clone the repository**
```bash
   git clone <repository-url>
   cd mini-blog-platform/backend
```

2. **Configure MySQL**
   
   Update `src/main/resources/application.properties`:
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/mini_blog_db
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
```

3. **Create database**
```sql
   CREATE DATABASE mini_blog_db;
```

4. **Install dependencies and run**
```bash
   mvn clean install
   mvn spring-boot:run
```

5. **Backend runs on** `http://localhost:8080`

6. **API Documentation** available at `http://localhost:8080/swagger-ui.html`

### Frontend Setup

1. **Navigate to frontend**
```bash
   cd ../frontend
```

2. **Install dependencies**
```bash
   npm install
```

3. **Create environment file**
```bash
   echo "NEXT_PUBLIC_API_URL=http://localhost:8080/api" > .env.local
```

4. **Run development server**
```bash
   npm run dev
```

5. **Frontend runs on** `http://localhost:3000`

## 📚 API Endpoints

### Authentication
```
POST   /api/auth/signup    - Register new user
POST   /api/auth/login     - Login user
GET    /api/auth/me        - Get current user
```

### Blogs
```
GET    /api/blogs          - Get all blogs
POST   /api/blogs          - Create blog (auth required)
GET    /api/blogs/{id}     - Get blog by ID
PUT    /api/blogs/{id}     - Update blog (author only)
DELETE /api/blogs/{id}     - Delete blog (author only)
```

### AI Support
```
POST   /api/ai/query       - Query AI assistant
GET    /api/ai/info        - Get AI agent info
```

## 🔒 Security Features

- ✅ BCrypt password hashing with salt
- ✅ JWT token authentication
- ✅ CORS configuration for frontend
- ✅ SQL injection prevention (JPA/Hibernate)
- ✅ XSS protection (React built-in)
- ✅ Input validation (Bean Validation + React Hook Form)
- ✅ Authorization checks for blog operations
- ✅ Global exception handling

## 🎯 Usage Examples

### 1. Sign Up
Navigate to `/auth/signup` and create an account with:
- Email
- Password (min 6 characters)
- Name (optional)

### 2. Create Blog
After login, click "Create Blog" and:
- Enter a title (max 200 characters)
- Write content (Markdown supported)
- Click "Publish"

### 3. Edit/Delete Blog
- Only you can edit or delete your own blogs
- Click "Edit" to modify
- Click "Delete" to remove (confirmation required)

### 4. AI Support
Navigate to `/support` and ask questions like:
- "How do I sign up?"
- "How to create a blog?"
- "Who can see my blogs?"

## 🧪 Testing

### Test User Flow
1. Open `http://localhost:3000`
2. Click "Sign Up" → Create account
3. Automatically logged in
4. Click "Create Blog" → Write and publish
5. View blog list on home page
6. Click blog to read full content
7. Edit your own blogs
8. Try AI Support for help

### Test API with Swagger
1. Open `http://localhost:8080/swagger-ui.html`
2. Test `/api/auth/signup` with sample data
3. Copy JWT token from response
4. Click "Authorize" → Enter `Bearer <token>`
5. Test other endpoints

## 🐛 Troubleshooting

### Backend Issues

**Port 8080 already in use:**
```bash
# Find and kill process
lsof -i :8080
kill -9 <PID>
```

**Database connection error:**
```bash
# Verify MySQL is running
mysql -u root -p
# Check credentials in application.properties
```

### Frontend Issues

**API connection error:**
```bash
# Check .env.local file
cat .env.local
# Should show: NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

**Auth token issues:**
```javascript
// Clear localStorage in browser console
localStorage.clear()
// Then login again
```

## 📈 Performance Optimizations

- Connection pooling with HikariCP
- Eager fetching for blog authors (JOIN FETCH)
- Transaction management with `@Transactional`
- Code splitting with Next.js App Router
- Memoization where needed
- Optimized bundle size

## 🚀 Deployment

### Backend Deployment
- **Heroku / Railway** - Easy deployment
- **AWS EC2** - Full control
- **DigitalOcean** - Simple VPS

### Frontend Deployment
- **Vercel** - Recommended for Next.js (one-click deploy)
- **Netlify** - Alternative option
- **AWS Amplify** - AWS integration

### Database
- **PlanetScale** - Serverless MySQL
- **AWS RDS** - Managed database
- **Railway** - Includes database

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- Your Name - *Initial work*

## 🙏 Acknowledgments

- Spring Boot Documentation
- Next.js Documentation
- Tailwind CSS
- Zustand State Management
- React Hook Form

## 📞 Support

For support, email your-email@example.com or create an issue in the repository.

---

**⭐ If you like this project, please give it a star!**

Made with ❤️ using Java Spring Boot & Next.js
EOF
