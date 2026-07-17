<img width="1536" height="1024" alt="ChatGPT Image Jul 17, 2026, 12_18_56 PM" src="https://github.com/user-attachments/assets/9389886e-4c3a-4a39-8f31-78c0a53a5859" /># Resume Builder

A modern, full-stack resume building application built with Spring Boot that allows users to create, manage, and customize professional resumes with multiple templates, image uploads, and payment integration.




## Features

- **User Authentication**
  - User registration and login with JWT-based authentication
  - Email verification system
  - Profile image upload via Cloudinary

- **Resume Management**
  - Create multiple resumes with custom templates
  - Comprehensive resume sections:
    - Profile information
    - Contact details
    - Work experience
    - Education
    - Skills with progress indicators
    - Projects with GitHub/live demo links
    - Certifications
    - Languages
    - Interests
  - Upload thumbnail and profile images for resumes
  - CRUD operations for resumes

- **Template System**
  - Multiple resume templates with customizable themes
  - Color palette selection

- **Payment Integration**
  - Razorpay integration for subscription plans
  - Support for basic and premium plans

- **Email Services**
  - Email verification using Brevo SMTP
  - Transactional email capabilities

## Tech Stack

- **Backend**: Spring Boot 3.5.16
- **Language**: Java 21
- **Database**: MongoDB
- **Authentication**: JWT (JSON Web Tokens)
- **Image Storage**: Cloudinary
- **Payment Gateway**: Razorpay
- **Email Service**: Brevo SMTP
- **Build Tool**: Maven
- **Library**: Lombok for boilerplate reduction

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- MongoDB 4.4+
- Cloudinary account (for image storage)
- Razorpay account (for payments)
- Brevo account (for email services)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/Akashsingh47304/Java_Project.git
cd resumeBuilder

Configure MongoDB:


bash
# Make sure MongoDB is running on localhost:27017
# Or update the connection string in application.properties
Configure application properties: Update src/main/resources/application.properties with your credentials:


properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/resumebuilder

# Cloudinary
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=604800000

# Razorpay
razorpay.key.id=your_razorpay_key_id
razorpay.key.secret=your_razorpay_key_secret

# Email (Brevo)
spring.mail.host=smtp-relay.brevo.com
spring.mail.port=587
spring.mail.username=your_brevo_username
spring.mail.password=your_brevo_password
Build the project:



bash
./mvnw clean install
Run the application:



bash
./mvnw spring-boot:run
The application will start on http://localhost:8080

API Endpoints
Authentication
POST /api/auth/register - Register new user
POST /api/auth/login - Login user
GET /api/auth/verify-email?token={token} - Verify email
POST /api/auth/upload-image - Upload profile image
GET /api/auth/profile - Get user profile
Resumes
POST /api/resume - Create new resume
GET /api/resume - Get all user resumes
GET /api/resume/{id} - Get resume by ID
PUT /api/resume/{id} - Update resume
PUT /api/resume/upload-image/{id} - Upload resume images
DELETE /api/resume/{id} - Delete resume
Templates
GET /api/templates - Get available templates
Payments
POST /api/payment/create-order - Create payment order
POST /api/payment/verify - Verify payment
Email
POST /api/email/send - Send email
Project Structure


src/main/java/com/Ak/resumeBuilder/
├── config/           # Configuration classes (Security, MongoDB, Cloudinary)
├── controller/       # REST controllers
├── document/         # MongoDB document models
├── dtos/            # Data transfer objects
├── exception/       # Custom exceptions and handlers
├── repository/      # MongoDB repositories
├── security/        # Security components (JWT, UserDetails)
├── service/         # Business logic services
└── utils/           # Utility classes (JWT utils)
Environment Variables
Set the following environment variables for production:



bash
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret
Security Features
JWT-based authentication
Password encryption
Email verification
Protected endpoints with role-based access
CORS configuration
Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

License
This project is licensed under the MIT License.

Author
Akash Singh - GitHub



