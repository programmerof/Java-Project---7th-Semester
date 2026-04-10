# Online Exam System — CSC409 Project

## Project Structure
```
OnlineExamSystem/
│
├── db/
│   └── schema.sql              ← Run this first in MySQL
│
├── src/
│   ├── util/
│   │   └── DBConnection.java   ← DB connection (edit credentials here)
│   ├── model/
│   │   └── User.java
│   ├── dao/
│   │   └── UserDAO.java
│   └── servlets/
│       ├── LoginServlet.java
│       ├── LogoutServlet.java
│       └── RegisterServlet.java
│
└── WebContent/
    ├── WEB-INF/
    │   └── web.xml
    └── jsp/
        ├── login.jsp
        └── register.jsp
```

## Setup Instructions

### Step 1 — MySQL Setup
```sql
-- Open MySQL command line or MySQL Workbench
source /path/to/db/schema.sql
```

### Step 2 — Configure DB Credentials
Edit `src/util/DBConnection.java`:
```java
private static final String DB_USER = "root";     // your MySQL username
private static final String DB_PASS = "";          // your MySQL password
```

### Step 3 — Add MySQL Connector JAR
Download `mysql-connector-j-8.x.jar` from:
https://dev.mysql.com/downloads/connector/j/

Add to your project's `WEB-INF/lib/` folder.

### Step 4 — Deploy on Tomcat
- Use Eclipse/IntelliJ → add to Tomcat server
- OR manually copy to `webapps/` folder
- Access at: http://localhost:8080/OnlineExamSystem/login

## Demo Login
| Role    | Email              | Password   |
|---------|--------------------|------------|
| Admin   | admin@exam.com     | admin123   |
| Student | ram@student.com    | student123 |

## Tech Stack
- Java Servlets + JSP (Jakarta EE / Tomcat 10)
- MySQL + JDBC
- Bootstrap 5 (CDN)
- Apache Tomcat 10
