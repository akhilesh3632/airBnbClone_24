# 🏠 Airbnb Backend Clone

A RESTful backend application inspired by Airbnb, built with **Spring Boot**.

This project provides APIs for user authentication, hotel management, room availability, bookings, guest management, hotel reporting, and online payments.

> 🚧 Backend-focused project — API development and backend architecture are the primary focus.



## 🚀 API Documentation

### 📚 Swagger UI

Explore and test all available REST APIs using Swagger/OpenAPI.

👉 **[Open Live Swagger UI](https://airbnbclone-24.onrender.com/api/v1/swagger-ui/index.html)**

The API is deployed and publicly accessible through Render.

### 🌐 Live API

👉 **[Open Live API](https://airbnbclone-24.onrender.com/api/v1/)**

### Local Swagger

After starting the application locally, open:

```text
http://localhost:8080/api/v1/swagger-ui/index.html
OpenAPI specification:

```text
http://localhost:8080/api/v1/v3/api-docs
```

---

## ✨ Features

- 🔐 User registration and authentication
- 🎟️ JWT-based authentication
- 👤 Guest management
- 🏨 Hotel creation and management
- 🛏️ Room management and availability
- 📅 Hotel booking system
- 👥 Add guests to bookings
- 💳 Stripe payment integration
- 📊 Hotel booking reports
- 🔒 Role-based authorization
- ⚠️ Centralized exception handling
- 🗄️ PostgreSQL database integration
- 📖 OpenAPI / Swagger documentation

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java | Programming Language |
| Spring Boot 3.5.7 | Backend Framework |
| Spring Web | REST APIs |
| Spring Data JPA | Database Access |
| Spring Security | Authentication & Authorization |
| JWT | Stateless Authentication |
| PostgreSQL | Relational Database |
| ModelMapper | DTO ↔ Entity Mapping |
| Stripe | Online Payments |
| Swagger / OpenAPI | API Documentation |
| Maven | Dependency Management |
| Lombok | Boilerplate Reduction |

---

## 🏗️ Architecture

The project follows a layered backend architecture:

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
PostgreSQL
```

Additional layers handle:

- DTOs for API request/response separation
- JWT authentication
- Global exception handling
- Entity relationships
- Payment processing

---

## 🔐 Authentication

Authentication is implemented using **JWT tokens**.

### Authentication Flow

```text
User
 │
 ├── Signup
 │
 └── Login
       │
       ▼
   Access Token
       │
       ▼
Authorization: Bearer <token>
       │
       ▼
JWT Authentication Filter
       │
       ▼
Protected API
```

The application also uses role-based authorization.

### Roles

- `GUEST`
- `HOTEL_MANAGER`

Hotel management APIs require the `HOTEL_MANAGER` role, while booking APIs require an authenticated user.

---

## 📌 API Overview

All APIs use the following base context path:

```text
/api/v1
```

### 🔑 Authentication APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/signup` | Register a new user |
| POST | `/auth/login` | Login and receive access token |
| POST | `/auth/refresh` | Generate a new access token |

---

### 🏨 Hotel Management APIs

Hotel management endpoints are protected and intended for hotel managers.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/admin/hotels` | Create a hotel |
| GET | `/admin/hotels` | Get all hotels |
| GET | `/admin/hotels/{hotelId}` | Get hotel by ID |
| PUT | `/admin/hotels/{hotelId}` | Update hotel |
| DELETE | `/admin/hotels/{hotelId}` | Delete hotel |
| PATCH | `/admin/hotels/{hotelId}/activate` | Activate hotel |
| GET | `/admin/hotels/{hotelId}/bookings` | Get hotel bookings |
| GET | `/admin/hotels/{hotelId}/reports` | Get hotel report |

---

### 📅 Booking APIs

Booking endpoints require authentication.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/bookings/init` | Initialize a booking |
| POST | `/bookings/{bookingId}/addGuests` | Add guests to a booking |
| POST | `/bookings/{bookingId}/payments` | Create payment session |

The booking flow checks room inventory before reserving rooms and creates a booking with the selected hotel, room, dates, and number of rooms.

---

## 💳 Payment Integration

The project integrates **Stripe Checkout** for online payments.

### Payment Flow

```text
Create Booking
      │
      ▼
Initialize Payment
      │
      ▼
Stripe Checkout Session
      │
      ▼
Complete Payment
```

The payment endpoint returns a Stripe checkout session URL that can be used by the client application.

---

## 🗄️ Database

The application uses **PostgreSQL** with **Spring Data JPA**.

Major entities include:

```text
User
 │
 ├── Hotel
 │     └── Room
 │          └── Inventory
 │
 └── Booking
        └── Guest
```

Bookings maintain relationships with hotels, rooms, users, and guests.

---

## 📂 Project Structure

```text
src/
└── main/
    └── java/
        └── com.maskara.airBnbApp/
            ├── controller/
            ├── service/
            ├── repository/
            ├── modal/
            ├── dto/
            ├── security/
            ├── exception/
            ├── advice/
            └── util/
```

### Main Layers

**Controller**

Handles HTTP requests and API endpoints.

**Service**

Contains application and business logic.

**Repository**

Handles database operations using Spring Data JPA.

**DTO**

Separates API request/response models from database entities.

**Security**

Contains JWT authentication, authorization, and password encryption.

**Advice / Exception**

Provides centralized API exception handling.

---

## ⚙️ Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/akhilesh3632/airBnbClone_24.git
cd airBnbClone_24
```

### 2. Configure PostgreSQL

Create a PostgreSQL database for the application.

Example:

```text
Database: airBnbApp
Port: 5432
```

### 3. Configure Environment Variables

The application uses environment variables for sensitive information.

Set the following variables:

```text
DB_PASSWORD=your_postgres_password
JWT_SECRET=your_jwt_secret
FRONTEND_URL=http://localhost:3000
```

> ⚠️ Never commit passwords, JWT secrets, Stripe keys, or other credentials to GitHub.

### 4. Build the project

```bash
mvn clean install
```

### 5. Run the application

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

## 📖 Using Swagger

Once the application is running, open:

```text
http://localhost:8080/api/v1/swagger-ui/index.html
```

Swagger allows you to:

- View all available endpoints
- See request parameters
- View request/response models
- Test APIs directly
- Test authenticated endpoints using JWT
- Explore the API without Postman

---

## 🔒 API Authorization

Protected endpoints require authentication.

Use the following HTTP header:

```http
Authorization: Bearer <your-access-token>
```

### Access Levels

```text
Public
 │
 └── Authentication APIs

Authenticated User
 │
 └── Booking APIs

Hotel Manager
 │
 └── Hotel Management APIs
```

---

## 🧪 API Testing

Recommended tools:

- Swagger UI
- Postman
- IntelliJ HTTP Client

Swagger is the easiest way to explore the API because the request and response models are generated directly from the application.

---

## 📈 Current Project Status

### Completed

- [x] User registration
- [x] User login
- [x] JWT authentication
- [x] Role-based authorization
- [x] Hotel management
- [x] Room management
- [x] Inventory / availability handling
- [x] Booking initialization
- [x] Guest management
- [x] Hotel booking reports
- [x] Stripe payment session
- [x] Swagger / OpenAPI documentation
- [x] Global exception handling

### Future Improvements

- [ ] Deploy backend publicly
- [ ] Add production database
- [ ] Add automated API tests
- [ ] Improve dynamic pricing
- [ ] Add booking cancellation
- [ ] Add reviews and ratings
- [ ] Add frontend integration
- [ ] Add Docker deployment

---

## 🎯 Project Goal

The goal of this project is to build a realistic backend system inspired by a real-world rental platform while practicing:

- REST API development
- Spring Boot
- Database design
- Authentication and authorization
- JWT security
- JPA relationships
- Transaction management
- API documentation
- Payment integration
- Layered backend architecture

---

## 👨‍💻 Author

**Akhilesh Chamoli**

Backend Developer | Java | Spring Boot | REST APIs

GitHub: [**@akhilesh3632**](https://github.com/akhilesh3632)



## ⭐ If you find this project useful

Feel free to explore the code, try the APIs through Swagger, and leave a ⭐ on the repository.