# College Seminar and Workshop Management Portal

A web application for managing college seminars and workshops, built with Java Servlets, JSP, JDBC, and MySQL.

## Technology Stack

| Layer      | Technology                          |
|------------|-------------------------------------|
| View       | JSP, HTML5, CSS3, JavaScript        |
| Controller | Java Servlets (`javax.servlet.*`)   |
| Model      | JavaBeans (`model` package)         |
| DAO        | JDBC with `java.sql.*`              |
| Database   | MySQL                               |
| Server     | Apache Tomcat 9.0.122               |

## Architecture: MVC Pattern

```
Request → Servlet (Controller) → DAO → Database
                ↓
           JSP (View) ← Model (JavaBeans)
```

---

## Project Structure

```
CollegeSeminarPortal/
├── src/
│   ├── model/          Event.java, Student.java, Registration.java
│   ├── dao/            EventDAO.java, StudentDAO.java, RegistrationDAO.java, AdminDAO.java
│   ├── controller/     All Servlet classes
│   └── util/           DBUtil.java
├── WebContent/
│   ├── *.jsp           All JSP pages
│   ├── css/style.css
│   ├── js/main.js
│   └── WEB-INF/
│       ├── web.xml
│       ├── classes/    Compiled .class files (auto-generated)
│       └── lib/        mysql-connector-j-26.7.0.jar
├── xml/
│   ├── events.xml      Sample XML data
│   ├── events.dtd      DTD validation
│   ├── events.xsd      XSD validation
│   ├── events.xsl      XSLT transformation
│   ├── DomParserDemo.java
│   ├── SaxParserDemo.java
│   └── XsltDemo.java
├── database/
│   ├── schema.sql
│   ├── sample-data.sql
│   └── stored-procedure.sql
├── db.properties       ← NEVER commit this file
├── build.bat
└── .gitignore
```

---

## Setup Instructions

### Step 1: Configure Database Credentials

Open `db.properties` and replace `YOUR_MYSQL_PASSWORD_HERE` with your actual MySQL root password:

```properties
db.url=jdbc:mysql://localhost:3306/seminar_portal?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
db.username=root
db.password=your_actual_password
```

### Step 2: Create the Database

Open a PowerShell or Command Prompt window and run:

```powershell
# Run schema (creates tables and stored procedure)
& "C:\Program Files\MySQL\MySQL Server 26.7\bin\mysql.exe" -u root -p < database\schema.sql

# Insert sample data (events, admin account, sample students)
& "C:\Program Files\MySQL\MySQL Server 26.7\bin\mysql.exe" -u root -p < database\sample-data.sql
```

Or in MySQL Workbench: open and run `schema.sql`, then `sample-data.sql`.

### Step 3: Build and Deploy

Make sure Tomcat is **stopped** before deploying (or use Tomcat's hot-reload).

Double-click `build.bat` or run it from PowerShell:

```powershell
.\build.bat
```

This compiles all Java files and copies everything to:
`C:\...\apache-tomcat-9.0.122\webapps\SeminarPortal\`

### Step 4: Start Tomcat

```powershell
& "C:\Users\DELL\Downloads\apache-tomcat-9.0.122-windows-x64\apache-tomcat-9.0.122\bin\startup.bat"
```

Wait a few seconds for Tomcat to start.

### Step 5: Open the Application

Open your browser and go to:
- **Home:** `http://localhost:8080/SeminarPortal/`
- **Events:** `http://localhost:8080/SeminarPortal/events`
- **Admin Login:** `http://localhost:8080/SeminarPortal/admin/login`

**Default Admin Credentials:**
- Username: `admin`
- Password: `Admin@1234`

---

## Pages and URLs

| URL | Description |
|-----|-------------|
| `/SeminarPortal/` | Home page |
| `/SeminarPortal/events` | Browse all events |
| `/SeminarPortal/register-event?eventId=1` | Register for event |
| `/SeminarPortal/my-registrations` | Student's registered events |
| `/SeminarPortal/admin/login` | Admin login |
| `/SeminarPortal/admin/dashboard` | Admin dashboard |
| `/SeminarPortal/admin/add-event` | Add new event |
| `/SeminarPortal/admin/edit-event?eventId=1` | Edit event |
| `/SeminarPortal/admin/view-registrations?eventId=1` | View registrations |
| `/SeminarPortal/admin/setup` | Create admin account |

---

## XML Demonstration

All XML files are in the `xml/` folder. Run from inside that folder:

```powershell
cd xml

# Compile
javac DomParserDemo.java
javac SaxParserDemo.java
javac XsltDemo.java

# Run DOM parser
java DomParserDemo

# Run SAX parser
java SaxParserDemo

# Run XSLT (creates events-output.html)
java XsltDemo
# Then open events-output.html in a browser
```

---

## JDBC Concepts Demonstrated

| JDBC Class | Where Used |
|-----------|-----------|
| `Statement` | `EventDAO.getAllEvents()` — simple query without parameters |
| `PreparedStatement` | All DAO methods with user input |
| `CallableStatement` | `RegistrationDAO.getRegistrationsByEvent()` — calls stored procedure |
| `ResultSet` | All SELECT queries |
| `Connection` | `DBUtil.getConnection()` |

---

## JSP Concepts Demonstrated

| Concept | File |
|---------|------|
| `<%@ page ... %>` directive | All JSP files |
| `<%@ include ... %>` directive | All JSP files (header/footer) |
| `<%= expression %>` | All JSP files |
| `<% scriptlet %>` | events.jsp, register-event.jsp etc. |
| `<%! declaration %>` | events.jsp (escapeHtml method) |
| `<jsp:useBean>` | register-event.jsp |
| `<jsp:getProperty>` | register-event.jsp (in comment) |
| Implicit objects (`request`, `response`, `session`, `out`) | All files |

---

## Security Features

- Passwords hashed with SHA-256 (not stored in plaintext)
- PreparedStatement used for all user-input queries (prevents SQL injection)
- Admin session guard on all `/admin/*` pages
- HTML escaping when displaying user-provided data
- Credentials stored in `db.properties` (excluded from Git)

---

## Git Setup (Basic Version Control)

```powershell
cd C:\Users\DELL\OneDrive\Desktop\Projects\CollegeSeminarPortal

# Initialize repository
git init

# Stage all files (db.properties is excluded by .gitignore)
git add .

# Verify db.properties is NOT staged
git status

# First commit
git commit -m "Initial commit: College Seminar Portal"
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Check mysql-connector jar is in WEB-INF/lib/ |
| `db.properties not found in classpath` | Ensure db.properties is in WEB-INF/classes/ |
| HTTP 404 on servlet URLs | Check @WebServlet annotation matches the URL |
| Login not working | Verify the SHA-256 hash in admins table matches `Admin@1234` |
| Compilation errors | Run build.bat from project root, check JDK path |
| MySQL connection refused | Ensure MySQL service is running |
