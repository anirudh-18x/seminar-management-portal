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

- PreparedStatement used for all user-input queries (prevents SQL injection)
- Admin session guard on all `/admin/*` pages
- HTML escaping when displaying user-provided data
- Credentials stored in `db.properties` (excluded from Git)

---
