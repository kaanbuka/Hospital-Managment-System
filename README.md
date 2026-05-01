# 🏥 ISUBU Hospital - Hospital Management System

A desktop hospital automation built in **Java Swing** with a **MySQL** backend, supporting role‑based access for four staff types (Admin, Doctor, Receptionist, Pharmacist). The application centralizes patient registration, doctor management, room/channel scheduling, prescriptions, inventory, and reporting in a single internal tool.

> **Status:** Educational / academic project. See [Known Limitations](#-known-limitations) before reusing in production.

---

## 📑 Table of Contents

- [Features](#-features)
- [Roles & Access Control](#-roles--access-control)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Database Schema](#-database-schema)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [Running the App](#-running-the-app)
- [Known Limitations](#-known-limitations)
- [Roadmap](#-roadmap)
- [License](#-license)

---

## ✨ Features

- 🔐 **Login & session** - Single login screen authenticates against the `user` table and routes the user to a role-aware dashboard.
- 👥 **User management** - Admins can create new staff accounts (Doctor / Receptionist / Pharmacist) with a username, password, and role.
- 🧑‍⚕️ **Patient module** - Add new patients and update existing patient records.
- 🩺 **Doctor module** - Maintain doctor profiles; list and view doctor details.
- 🏥 **Channels (rooms / appointments)** — Create channels and view existing ones, scoped per logged-in user.
- 💊 **Prescriptions** - Issue prescriptions and view prescription history.
- 📦 **Inventory & items** - Track hospital items / supplies through dedicated inventory screens.
- 📊 **Reports** — Pharmacist-side reporting view for operational summaries.

---

## 🛡 Roles & Access Control

The dashboard (`Main.java`) hides or shows menu buttons based on the logged-in user's `utype`:

| Module           | Admin | Doctor | Receptionist | Pharmacist |
| ---------------- | :---: | :----: | :----------: | :--------: |
| Patient          |  ✅   |   ❌   |      ✅      |     ❌     |
| Doctor           |  ✅   |   ✅   |      ❌      |     ❌     |
| Create Room      |  ✅   |   ❌   |      ✅      |     ❌     |
| View Room        |  ✅   |   ✅   |      ✅      |     ❌     |
| View Prescription|  ✅   |   ❌   |      ❌      |     ✅     |
| Create Item      |  ✅   |   ✅   |      ✅      |     ❌     |
| Create User      |  ✅   |   ❌   |      ❌      |     ❌     |
| View Doctor      |  ✅   |   ✅   |      ✅      |     ✅     |
| Report           |  ❌   |   ❌   |      ❌      |     ✅     |
| Logout           |  ✅   |   ✅   |      ✅      |     ✅     |

Authorization is **enforced on the dashboard navigation** (`Main`) — restricted modules are hidden from the menu, and `Doctor` access is additionally gated at click-time with an `Unauthorized access` dialog.

---

## 🏗 Architecture

The project follows a simple **two-tier client–server pattern**:

```
┌──────────────────────────────────────────┐
│           Java Swing Desktop UI          │
│   (Login → Main dashboard → Modules)     │
└─────────────────────┬────────────────────┘
                      │ JDBC
                      ▼
┌──────────────────────────────────────────┐
│   ConnectionProvider (DriverManager)     │
└─────────────────────┬────────────────────┘
                      │
                      ▼
┌──────────────────────────────────────────┐
│        MySQL  —  loyalhospital DB        │
│  user · patient · doctor · channel ·     │
│  item · inventory · prescription · …     │
└──────────────────────────────────────────┘
```

- **UI layer:** Each domain (`Patient`, `Doctor`, `Channel`, `Item`, …) is a `JFrame` generated with NetBeans GUI builder (`initComponents`).
- **Data access layer:** A single static helper `ConnectionProvider.getCon()` returns a JDBC `Connection`. Every form opens its own connection, runs its query, and closes locally.
- **Look & feel:** Nimbus is applied at startup in each entry point.

---

## 🧰 Tech Stack

| Layer    | Technology                                      |
| -------- | ----------------------------------------------- |
| Language | Java (JDK 8+)                                   |
| UI       | Java Swing (NetBeans GUI Builder, Nimbus L&F)   |
| Database | MySQL 8.x                                       |
| Driver   | MySQL Connector/J (`com.mysql.cj.jdbc.Driver`)  |
| IDE      | Apache NetBeans                                 |
| Paradigm | OOP, CRUD, JDBC                                 |

---

## 📂 Project Structure

```
Hospital-Managment-System/
├── Login.java               # Authentication screen
├── Main.java                # Role-aware dashboard
├── ConnectionProvider.java  # JDBC connection helper
│
├── User.java                # Create staff users (admin only)
├── Patient.java             # Add / update patient
├── Doctor.java              # Manage doctor profile
├── viewDoctor.java          # Browse all doctors
│
├── Channel.java             # Create room / appointment channel
├── viewChannel.java         # View user's channels
│
├── Item.java                # Add inventory item
├── Inventory.java           # Inventory listing
│
├── Prescription.java        # Issue prescription
├── viewPrescription.java    # Prescription history
│
└── Report.java              # Pharmacist-side reporting
```

---

## 🗄 Database Schema

The application connects to a MySQL schema named **`loyalhospital`**. Below is the minimum schema required to run the app, reverse-engineered from the JDBC calls in the codebase. Adjust column types and add `AUTO_INCREMENT` / `PRIMARY KEY` as appropriate.

```sql
CREATE DATABASE IF NOT EXISTS loyalhospital;
USE loyalhospital;

-- Staff accounts
CREATE TABLE IF NOT EXISTS user (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    _name       VARCHAR(100),
    username    VARCHAR(50) UNIQUE,
    _password   VARCHAR(255),
    utype       ENUM('Admin', 'Doctor', 'Receptionist', 'Pharmacist')
);

-- Seed an initial admin so you can log in the first time
INSERT INTO user (_name, username, _password, utype)
VALUES ('System Admin', 'admin', 'changeme', 'Admin');

-- Add the remaining tables based on the modules you need:
-- patient, doctor, channel, item, inventory, prescription, report …
```

> 💡 The exact column lists for the other tables (`patient`, `doctor`, `channel`, `item`, `prescription`, …) match the form fields in the corresponding `*.java` files. Open the file, look at the `INSERT INTO …` query inside the "Add" button handler, and create the columns accordingly.

---

## 🚀 Getting Started

### Prerequisites

- ☕ **JDK 8 or newer**
- 🐬 **MySQL 8.x** running locally (or remote)
- 🔌 **MySQL Connector/J** JAR ([download](https://dev.mysql.com/downloads/connector/j/))
- 🛠 **Apache NetBeans** (recommended — the project uses NetBeans GUI forms) or any Java IDE that can render `.form` files

### 1. Clone the repository

```bash
git clone https://github.com/kaanbuka/Hospital-Managment-System.git
cd Hospital-Managment-System
```

### 2. Create the database

Open MySQL and run the `CREATE DATABASE / CREATE TABLE` script from the [Database Schema](#-database-schema) section.

### 3. Open in NetBeans

- `File → Open Project` and select the cloned folder.
- Right-click the project → `Properties → Libraries → Add JAR/Folder` and add the **MySQL Connector/J** JAR.

---

## ⚙ Configuration

Database credentials live in `ConnectionProvider.java`. **Before running the project, replace the connection string with your own credentials.**

```java
// ConnectionProvider.java
public static Connection getCon() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/loyalhospital",
            "YOUR_DB_USER",
            "YOUR_DB_PASSWORD"
        );
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Disconnect!");
    }
    return null;
}
```

> 🔒 **Security note:** Credentials should not be committed to a public repository. For a production-grade fix, load them from environment variables or an external `db.properties` file that is git-ignored.

---

## ▶ Running the App

1. In NetBeans, right-click `Login.java` → **Run File**, or set it as the project's main class and press **Run** (`F6`).
2. Log in with the admin you seeded (`admin` / `changeme`).
3. From the dashboard, create additional users (Doctor / Receptionist / Pharmacist) and start using the modules.

---

## ⚠ Known Limitations

These limitations exist because the project was built as a **learning exercise** for OOP, Swing, and JDBC. They are intentionally documented so they can be tracked as improvements.

- **🛑 SQL Injection:** Most data-access code uses `Statement` with concatenated strings (`"INSERT INTO user … '" + name + "'"`). All input flows from the UI directly into the query.
  → *Fix:* migrate every query to `PreparedStatement` with parameter placeholders (`?`).

- **🛑 Plaintext passwords:** The `_password` column stores user passwords as raw strings.
  → *Fix:* hash with **BCrypt** (or Argon2) at user-creation time and verify hashes on login.

- **🛑 Hard-coded credentials:** DB host, user, and password are baked into `ConnectionProvider`.
  → *Fix:* externalize to a `db.properties` file (git-ignored) or read from environment variables.

- **🛑 No connection pooling:** Every form opens a fresh JDBC `Connection` and rarely closes it explicitly.
  → *Fix:* introduce a single pool (e.g., **HikariCP**) and use try-with-resources for `Connection`, `Statement`, and `ResultSet`.

- **⚠ Authorization is UI-only:** Role enforcement happens by hiding buttons on the dashboard. A determined user with DB access could bypass it.
  → *Fix:* check role server-side / inside each form on entry.

- **⚠ Hard-coded asset paths:** Some `JLabel` icons reference an absolute macOS path (`/Users/kahansmacbook/Downloads/HMS ICON/…`).
  → *Fix:* move images into `src/main/resources/` and load via `getClass().getResource(...)`.

- **⚠ Mixed naming conventions:** Some classes use lowercase first letters (`viewDoctor`, `viewChannel`) which violates Java naming convention.
  → *Fix:* rename to `ViewDoctor`, `ViewChannel`, etc.

---

## 🗺 Roadmap

Realistic next steps if the project were to be matured:

- [ ] Replace all `Statement` calls with `PreparedStatement`.
- [ ] Hash passwords with BCrypt at registration and login.
- [ ] Externalize DB config to a `.properties` file + add `.gitignore`.
- [ ] Add a `Patient → Doctor → Channel` foreign-key model and proper indexes.
- [ ] Introduce **HikariCP** for connection pooling.
- [ ] Add unit tests for the data layer (e.g., **JUnit 5** + **H2** in-memory DB).
- [ ] Move resources (icons, backgrounds) into the project's `resources` folder.
- [ ] Migrate UI to **JavaFX** or rewrite the backend as a REST API consumed by a web frontend.

---

## 📜 License

This project is released for educational purposes. Feel free to fork, study, and adapt it. If you reuse it in coursework or a portfolio, an attribution back to this repository is appreciated.
