
# Access Attendance System – Automated Time Tracking Solution

An attendance management system built for workplaces, schools, and hospitality environments.
Designed to log check-ins and check-outs using facial recognition or QR codes, ensuring secure, timely, and accurate attendance records.

---

## 🔥 What's Inside

**Backend**
Built with Spring Boot and MySQL, featuring:

* Google or email-based login
* JWT authentication
* Role-based access control
* Real-time attendance validation
* Email-based QR code delivery

---

## ⚡ Project Structure

```
access-attendance-system/
├── backend/        # Spring Boot API
└── README.md       # Project overview (this file)
```

---

## ✨ Key Features

* Staff can register using Google login or email/password
* After profile setup (face image + work hours), a unique QR code is sent via email for daily use
* Supports two methods for attendance:

  * Facial recognition (face image match)
  * QR code scan
* Attendance rules:

  * Check-in allowed up to 1 hour early or late
  * Checkout allowed up to 10 minutes late
* Secure JWT authentication for all endpoints
* Real-time validation of check-in/check-out eligibility
* Admin dashboard to:

  * Register and manage staff (receptionists, cleaners, etc.)
  * Assign roles and working hours
  * View full attendance logs
* Complete audit trail for every attendance action

---

## 🚀 Getting Started

### Clone the Project

```bash
git clone https://github.com/austinendlovu/Access-Attendance-System.git
cd access-attendance-system/backend
```

---

### Configure the Backend

1. Open `application.properties` or `application.yml`

2. Add database and email configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/access_attendance
    username: your_db_username
    password: your_db_password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

---

### Run the Backend

```bash
mvn clean install
mvn spring-boot:run
```

* API runs at:
  `http://localhost:8080`

* Swagger Docs available at:
  `http://localhost:8080/swagger-ui/index.html`

---

## 🌍 Deployment

**Backend** can be deployed to:

* Railway, Heroku, or VPS
* Cloud providers like AWS, Azure, or GCP

---

## 🤝 Contributing

* Fork the repository
* Create a feature branch:
  `git checkout -b feature/your-feature`
* Commit and push your changes
* Open a Pull Request

---

## 📜 License

MIT License

---

## 📬 Support

Open an issue on GitHub to ask questions, suggest improvements, or collaborate on better attendance systems.

