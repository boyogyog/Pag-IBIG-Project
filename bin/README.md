# Pag-CONNECT

**Pag-CONNECT** is a desktop-based Pag-IBIG membership management system built using Java (Swing) and MySQL. This project demonstrates the application of core OOP principles, database connectivity using JDBC, exception handling, and CRUD operations through a GUI-driven interface.

---

## Project Overview

This system is developed using Java and MySQL, fulfilling the requirements for OOP and Information Management courses.

It allows users to perform basic system transactions such as:

- Add Records
- View Records
- Update Records
- Delete Records

Across the following membership form modules:

- Member Information
- Current Employment Record
- Previous Employment Record
- Heirs / Dependents
- Company Details

The project demonstrates the use of:

- Classes and Objects
- Encapsulation
- Inheritance
- Polymorphism
- Abstraction
- Constructors and Methods
- Exception Handling
- Database Connectivity (JDBC)

---

## Technologies Used

| Technology         | Purpose                    |
| ------------------ | -------------------------- |
| Java 8             | Main Programming Language  |
| Java Swing         | GUI / Desktop Interface    |
| MySQL 9.7          | Database                   |
| JDBC               | Java Database Connectivity |
| Eclipse / IntelliJ | IDE                        |
| GitHub             | Version Control            |

---

## Project Structure

```
Pag-CONNECT-Project/
└── src/main/java/
    ├── main/
    │   └── Pagibig1.java                   ← Entry point, launches the app
    ├── ui/
    │   ├── LoginPanel.java                 ← Login screen
    │   ├── DashboardFrame.java             ← Module selection dashboard
    │   ├── MemberInfoForm.java             ← Member personal details form
    │   ├── CurrentEmpForm.java             ← Current employment form
    │   ├── PrevEmpForm.java                ← Previous employment form
    │   └── HeirsForm.java                  ← Heirs / dependents form
    └── database/
        ├── dao/
        │   ├── MemberDAO.java              ← CRUD for membertable
        │   ├── CompanyDAO.java             ← CRUD for companydetailstable
        │   ├── CurrentEmpDAO.java          ← CRUD for currentemprecordtable
        │   ├── HeirsDAO.java               ← CRUD for heirstable
        │   └── PrevEmpDAO.java             ← CRUD for prevemptable
        ├── database/
        │   └── DatabaseConnection.java     ← MySQL connection manager
        ├── models/
        │   ├── MemberTable.java
        │   ├── CompanyDetailsTable.java
        │   ├── CurrentEmpRecordTable.java
        │   ├── HeirsTable.java
        │   └── PrevEmpTable.java
        └── migrations/
            ├── 001_add_record.sql
            ├── 002_add_record.sql
            ├── 003_add_record.sql
            └── 004_add_record.sql
```

---

## Package Descriptions & Team Responsibilities

---

### 🖥️ Frontend / UI — `ui/`

**Assigned to: Raven Rayo, James Patrick Isidro**

Contains all GUI screens built with Java Swing. Each file represents one screen or form that the user interacts with. The UI layer never accesses the database directly — it always calls a DAO method to perform any data operation.

| File                  | Description                              |
| --------------------- | ---------------------------------------- |
| `LoginPanel.java`     | Login screen shown on startup            |
| `DashboardFrame.java` | Main menu displaying all form modules    |
| `MemberInfoForm`      | Details about the membership application |
| `...Form`             | Stated in documents                      |

---

## Application Architecture

```
UI Layer (ui/)
     ↓  calls
DAO Layer (database/dao/)
     ↓  uses
DatabaseConnection (database/database/)
     ↓  connects to
MySQL Database
```

Models (`database/models/`) serve as data containers passed between the UI and DAO layers.

---

## Database Schema

The system uses the `pagibig` MySQL database with the following tables:

| Table                   | Description                                  |
| ----------------------- | -------------------------------------------- |
| `membertable`           | Core member personal and contact information |
| `companydetailstable`   | Company records linked to members            |
| `currentemprecordtable` | Each member's current employment details     |
| `prevemptable`          | Previous employment history per member       |
| `heirstable`            | Heirs and dependents per member              |

---

## Required Software Installation

### 1. Java JDK

Download and install **Java 8 (JavaSE-1.8)**:

- [Eclipse Temurin JDK](https://adoptium.net/)

Verify installation:

```bash
java -version
```

### 2. IDE

- [Eclipse IDE](https://www.eclipse.org/downloads/) — recommended
- or [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### 3. MySQL

- Download [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
- Download [MySQL Workbench](https://dev.mysql.com/downloads/workbench/) for GUI management

### 4. JDBC Driver

- Download [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
- Add the `.jar` file to your project's build path in Eclipse/IntelliJ
- No need for this since it's already configured in the pom.xml, and Eclipse has its respective libraries already.

### 5. GitHub

- Set up Git inside your IDE or install [Git](https://git-scm.com/downloads) separately

---

## Database Setup

1. Open MySQL Workbench or any MySQL client
2. Create the database:

```sql
CREATE DATABASE pagibig;
```

3. Import the schema:

```bash
mysql -u root -p pagibig < database/pagibig.sql
```

4. (Optional) Import seed data:

```bash
mysql -u root -p pagibig < database/seed.sql
```

5. Run adding_records, all migrations

---

## Configuration

Open `DatabaseConnection.java` and update the credentials to match your local MySQL setup:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/pagibig";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password_here";
```

---

## Collaborating on this Repository

### Create Your Branch

- In Eclipse, right click then navigate to team, then there should be a push branch option.

### Pull Latest Changes Before Working

```bash
git pull origin main
```

### Push Your Branch

```bash
git push origin feature-yourname
```

### Commit Message Format

```bash
git commit -m "Added member CRUD functionality"
```

---

## Contributors

| Name                 |
| -------------------- |
| Yeshua Arjona        |
| James Escanillas     |
| James Patrick Isidro |
| Raven Rayo           |
| Migel Tan            |

---

## License

Academic use only. Developed as a course requirement for OOP and Information Management.
