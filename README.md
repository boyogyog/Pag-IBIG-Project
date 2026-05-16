# PagCONNECT

PagCONNECT is a web-based Pag-IBIG membership management system built using Laravel and MySQL. The project is designed to manage member records, contributions, membership information, and related services through a centralized web application.

## Tech Stack

* Backend: PHP / Laravel
* Database: MySQL
* Frontend: Blade Templates / HTML / CSS / JavaScript
* Version Control: Git + GitHub

---

# Project Setup Guide

## 1. Clone the Repository

```bash
git clone <your-repository-url>
cd pagconnect
```

---

## 2. Install PHP Dependencies

Make sure Composer is installed.

```bash
composer install
```

This will generate the `vendor/` folder required by Laravel.

---

## 3. Create the Environment File

Copy the example environment file:

### Windows PowerShell

```powershell
copy .env.example .env
```

### macOS / Linux

```bash
cp .env.example .env
```

---

## 4. Configure Database Connection

Open the `.env` file and edit the database section:

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=pagibig
DB_USERNAME=root
DB_PASSWORD=
```

Make sure the database already exists in MySQL.

---

## 5. Import the Database

The SQL dump is located at:

```txt
database/sql/pagibig.sql
```

### Using MySQL Workbench

1. Open MySQL Workbench
2. Create a schema named:

```txt
pagibig
```

3. Go to:

```txt
Server → Data Import
```

4. Choose:

```txt
Import from Self-Contained File
```

5. Select:

```txt
database/sql/pagibig.sql
```

6. Select target schema:

```txt
pagibig
```

7. Click:

```txt
Start Import
```

---

## 6. Generate Laravel Application Key

```bash
php artisan key:generate
```

---

## 7. Run the Application

```bash
php artisan serve
```

Open the generated local URL in your browser.

Example:

```txt
http://127.0.0.1:8000
```

---

# Git Workflow

## Pull Latest Changes

```bash
git pull
```

## Add Changes

```bash
git add .
```

## Commit Changes

```bash
git commit -m "Your message here"
```

## Push Changes

```bash
git push
```

---

# Important Notes

## Do NOT Upload These Files/Folders

The following should remain excluded from GitHub:

```txt
/vendor
/node_modules
.env
```

---

## Upload These Files

Important project files that SHOULD be uploaded:

```txt
app/
routes/
resources/
database/sql/pagibig.sql
.env.example
composer.json
```

---

# Recommended Development Practices

* Do not manually edit the production database.
* Keep database updates synchronized with the team.
* Use Git commits with clear messages.
* Test features locally before pushing.
* Pull latest changes before starting development.


# License

This project is for academic and educational purposes only.
