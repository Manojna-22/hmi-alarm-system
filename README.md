# HMI Alarm Acknowledgement Board

> Industry-grade HMI alarm management system — Spring Boot 3 + Hibernate + MySQL + Angular 17

---

## Tech Stack

| Layer     | Technology                              |
|-----------|-----------------------------------------|
| Backend   | Spring Boot 3.2, Hibernate, Spring Data JPA |
| Database  | MySQL 8.x via SQL Workbench             |
| Frontend  | Angular 17 (standalone components)     |
| Mapping   | MapStruct                               |
| Build     | Maven 3.9.x / Node 18+                 |
| JDK       | 17                                      |

---

## Project Structure

```
hmi-alarm-system/
├── backend/          ← Spring Boot project
│   ├── pom.xml
│   └── src/main/java/com/hmi/alarm/
│       ├── config/       WebConfig, DataSeeder
│       ├── controller/   AlarmController, AlarmEventController
│       ├── dto/          All request/response DTOs
│       ├── entity/       Alarm, AlarmEvent, enums
│       ├── exception/    Custom exceptions + GlobalExceptionHandler
│       ├── mapper/       AlarmMapper (MapStruct)
│       ├── repository/   JPA repositories
│       └── service/      AlarmService, AlarmEventService + impls
└── frontend/         ← Angular project
    └── src/app/
        ├── core/         Models, AlarmService, interceptors
        ├── features/     alarm-board, alarm-detail
        └── shared/       SeverityBadge component
```

---

## Setup Instructions

### Step 1 — MySQL Database

Open **MySQL Workbench**, connect to localhost, and run:

```sql
CREATE DATABASE hmi_alarm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> If your MySQL password is not `root`, update `application.properties`:
> `spring.datasource.password=YOUR_PASSWORD`

---

### Step 2 — Backend (Spring Boot)

Open a terminal in VSCode (`Ctrl+`` `):

```bash
cd hmi-alarm-system/backend

# Build and run (Maven 3.9.x with JDK 17)
mvn clean install -DskipTests
mvn spring-boot:run
```

Backend will start at: **http://localhost:8080**

Hibernate will auto-create tables on first run.  
Sample alarm data is seeded automatically.

**Verify it works:**
```
GET http://localhost:8080/api/v1/alarms/stats
GET http://localhost:8080/api/v1/alarms/active
GET http://localhost:8080/actuator/health
```

---

### Step 3 — Frontend (Angular)

Open a **second terminal** in VSCode:

```bash
cd hmi-alarm-system/frontend

# Install dependencies (first time only)
npm install

# Start dev server
npm start
```

Frontend will start at: **http://localhost:4200**

Make sure the backend is running before opening the frontend.

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/v1/alarms` | List all alarms (pagination + filter) |
| POST   | `/api/v1/alarms` | Create a new alarm |
| GET    | `/api/v1/alarms/{id}` | Get alarm by ID |
| GET    | `/api/v1/alarms/active` | Get all active alarms |
| GET    | `/api/v1/alarms/stats` | Dashboard stats |
| PATCH  | `/api/v1/alarms/{id}/acknowledge` | Acknowledge alarm |
| PATCH  | `/api/v1/alarms/{id}/clear` | Clear alarm |
| DELETE | `/api/v1/alarms/{id}` | Delete alarm |
| GET    | `/api/v1/alarms/{id}/events` | Get event timeline |

---

## Push to GitHub

See the GitHub section in the handoff document.
