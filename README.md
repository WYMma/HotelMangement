# Hotel Management System

A comprehensive web-based hotel management system built with Java EE technologies. This system allows hotel management, guest bookings, and agent operations.

## Technologies Used

- Java 21
- Jakarta EE 10 (Servlet, JSP)
- MySQL 8.2
- Maven
- JSTL 3.0
- SLF4J & Logback for logging
- Commons IO

## Features

- **Visitor Features**
  - Browse available hotels
  - View hotel details
  - Search functionality
  - Make reservations

- **Agent Dashboard**
  - Manage hotel properties
  - Handle bookings
  - View statistics

- **Admin Features**
  - Manage agent accounts (Create, Update, Delete users)
  - Monitor and manage all hotel properties

## Prerequisites

- JDK 21 or higher
- MySQL 8.2 or higher
- Maven
- A Jakarta EE compatible application server (e.g., Tomcat 10+)

## Setup

1. Clone the repository
2. Import the database schema:
   ```bash
   mysql -u your_username -p < hotel_management.sql
   ```

3. Configure the database connection in your application server's configuration

4. Build the project:
   ```bash
   mvn clean install
   ```

5. Deploy the generated WAR file to your application server

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── hotel/
│   │           ├── servlet/      # Servlet controllers
│   │           └── ...          # Other Java packages
│   └── webapp/
│       └── WEB-INF/
│           └── jsp/             # JSP views
```

## Database Schema

The project uses a MySQL database with tables for:
- Hotels
- Rooms
- Bookings
- Users (Visitors & Agents)

The complete schema can be found in `hotel_management.sql`
