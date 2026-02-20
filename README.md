# 🐾 PetCare Management Platform

A full-stack pet care management system built with Spring Boot and Vue 3, featuring department management, employee management, customer management, data visualization, and JWT authentication.

## 🛠 Tech Stack

### Backend
- **Java 19** + **Spring Boot 3.5**
- **MyBatis** (Dynamic SQL, XML Mapper)
- **MySQL 8.0**
- **JWT** (JSON Web Token for authentication)
- **PageHelper** (Pagination)
- **Interceptor** (Token validation)
- **Alibaba Cloud OSS** (File upload)

### Frontend
- **Vue 3** (Composition API + `<script setup>`)
- **Element Plus** (UI components)
- **ECharts** (Data visualization)
- **Axios** (HTTP client)

### DevOps & Tools
- **Docker** (MySQL, Nginx containers)
- **Nginx** (Frontend deployment)
- **Maven** (Build tool)
- **Git / GitHub** (Version control)

## 📋 Features

### 🏢 Department Management
- CRUD operations (Create, Read, Update, Delete)

### 👨‍💼 Employee Management
- Paginated list with conditional search (name, gender, date range)
- Add / Edit / Batch Delete employees
- Employee work experience management
- Employee statistics (job distribution bar chart, gender distribution pie chart)

### 👥 Customer (Pet Owner) Management
- Paginated list with conditional search (name, gender, date range)
- Add / Edit / Delete customers
- Customer statistics (gender distribution pie chart, address distribution bar chart)

### 🔐 Authentication
- Login with username and password
- JWT token generation and validation
- Interceptor-based token verification for all API requests
- Auto logout on token expiration (401 response)

### 📊 Data Visualization
- Employee job distribution (Bar Chart)
- Employee gender distribution (Pie Chart)
- Customer gender distribution (Pie Chart)
- Customer address distribution (Bar Chart)

## 📁 Project Structure

```
petcare-management-platform/
├── src/main/java/com/han/
│   ├── config/            # CorsConfig, WebConfig
│   ├── controller/        # REST API controllers
│   ├── interceptor/       # JWT token interceptor
│   ├── mapper/            # MyBatis mapper interfaces
│   ├── pojo/              # Entity classes (Emp, Owner, Dept, etc.)
│   ├── service/           # Business logic layer
│   ├── utils/             # JwtUtils
│   └── exception/         # Global exception handler
├── src/main/resources/
│   ├── com/han/mapper/    # MyBatis XML mappers
│   └── application.properties
├── petcare-frontend/      # Vue 3 frontend
│   ├── src/
│   │   ├── App.vue        # Main component
│   │   └── main.js        # Entry point
│   ├── package.json
│   └── vite.config.js
└── pom.xml
```

## 🚀 Getting Started

### Prerequisites
- Java 19+
- Maven
- MySQL 8.0
- Node.js 18+
- Docker (optional, for MySQL and Nginx)

### Backend Setup

1. Clone the repository:
```bash
git clone https://github.com/limyooo/petcare-management-platform.git
cd petcare-management-platform
```

2. Create MySQL database:
```sql
CREATE DATABASE petcare;
```

3. Update `src/main/resources/application.properties` with your MySQL credentials.

4. Run the application:
```bash
mvn spring-boot:run
```

Backend will start at `http://localhost:8080`

### Frontend Setup

```bash
cd petcare-frontend
npm install
npm run dev
```

Frontend will start at `http://localhost:5173`

### Build for Production

```bash
# Backend
mvn package

# Frontend
cd petcare-frontend
npm run build
```

## 📸 Screenshots

### Login Page
- JWT-based authentication with username and password

### Department Management
- Full CRUD operations with real-time updates

### Employee Management
- Paginated table with search filters (name, gender, hire date)

### Customer Management
- Paginated table with search filters (name, gender, created date)

### Data Visualization
- Interactive charts powered by ECharts

## 🗄 Database Tables

| Table | Description |
|-------|-------------|
| `pet_department` | Department information |
| `emp` | Employee information |
| `emp_expr` | Employee work experience |
| `emp_log` | Employee operation logs |
| `pet_owner` | Customer (pet owner) information |

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/login` | User login |
| GET | `/api/depts` | Get all departments |
| POST | `/api/depts` | Add department |
| PUT | `/api/depts` | Update department |
| DELETE | `/api/depts/{id}` | Delete department |
| GET | `/api/emps` | Get employees (paginated) |
| POST | `/api/emps` | Add employee |
| PUT | `/api/emps` | Update employee |
| DELETE | `/api/emps?ids=` | Delete employees |
| GET | `/api/emps/{id}` | Get employee detail |
| GET | `/api/owners` | Get customers (paginated) |
| POST | `/api/owners` | Add customer |
| PUT | `/api/owners` | Update customer |
| DELETE | `/api/owners?ids=` | Delete customers |
| GET | `/api/report/empJobData` | Employee job statistics |
| GET | `/api/report/empGenderData` | Employee gender statistics |
| GET | `/api/report/ownerGenderData` | Customer gender statistics |
| GET | `/api/report/ownerAddressData` | Customer address statistics |

## 📝 License

This project is for learning purposes.
