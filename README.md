<div align="center">

# ⚡ Employee & Intern API

**Serverless HR Back-End · Spring Boot 3 · AWS Lambda · PostgreSQL**

<br>

<img src="https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
<img src="https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot">
<img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
<img src="https://img.shields.io/badge/Neon-00E599?style=for-the-badge&logo=neon&logoColor=white" alt="Neon">
<img src="https://img.shields.io/badge/AWS_Lambda-FF9900?style=for-the-badge&logo=aws-lambda&logoColor=white" alt="AWS Lambda">
<img src="https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white" alt="Flyway">
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle">

<br>

> Full CRUD REST API for managing employees and interns, deployed serverlessly on AWS Lambda with PostgreSQL on [Neon](https://neon.tech).

</div>

---

## 🔗 Live

| Layer | URL |
|-------|-----|
| **API** | [`https://cvsnvjrn6rftkml3hrt673n5qe0geobn.lambda-url.eu-west-3.on.aws`](https://cvsnvjrn6rftkml3hrt673n5qe0geobn.lambda-url.eu-west-3.on.aws) |
| **Admin UI** | [`https://employees-admin-mauve.vercel.app`](https://employees-admin-mauve.vercel.app) |

```sh
# Quick health check
curl https://cvsnvjrn6rftkml3hrt673n5qe0geobn.lambda-url.eu-west-3.on.aws/ping
# → pong
```

---

## 🧬 Tech Stack

| Layer | Stack |
|-------|-------|
| **Runtime** | Java 21 · Spring Boot 3.2.2 · Spring Data JPA · Hibernate 6 |
| **Database** | PostgreSQL ([Neon](https://neon.tech) serverless) · Flyway 9.22 |
| **Frontend** | [React-Admin 5](https://marmelab.com/react-admin/) · TypeScript · Material UI · hosted on Vercel |

---

## 📡 API

### Employees `/employees`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/employees` | List (paginated, filterable, searchable) |
| `GET` | `/employees?id=1&id=2` | Get batch by IDs |
| `GET` | `/employees/{id}` | Get one |
| `POST` | `/employees` | Create |
| `PUT` | `/employees/{id}` | Full update |
| `PATCH` | `/employees/{id}` | Partial update |
| `DELETE` | `/employees/{id}` | Delete (unassigns interns) |

### Interns `/interns`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/interns` | List (paginated, filterable) |
| `GET` | `/interns?id=1&id=2` | Get batch by IDs |
| `GET` | `/interns/manager/{managerId}` | Get by manager |
| `GET` | `/interns/{id}` | Get one |
| `POST` | `/interns` | Create |
| `PUT` | `/interns/{id}` | Full update |
| `PATCH` | `/interns/{id}` | Partial update |
| `DELETE` | `/interns/{id}` | Delete |

### Health

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/ping` | `→ pong` |
| `GET` | `/health/db` | DB connectivity check |
| `GET` | `/health/email?to=...` | Send SES test email |

> Pagination via `_start`/`_end`, sorting via `_sort`/`_order` (React-Admin compatible).  
> Results include `X-Total-Count` header.

---

## 🏛️ Schema

```
employee                        intern
├── id          BIGINT PK       ├── id             BIGINT PK
├── first_name  VARCHAR NOT NULL ├── first_name     VARCHAR NOT NULL
├── email       VARCHAR UNIQUE   ├── email          VARCHAR UNIQUE
├── department  VARCHAR(enum)    ├── department     VARCHAR(enum)
├── salary      DECIMAL          ├── salary         DECIMAL
└── is_active   BOOLEAN          ├── is_remunerate  BOOLEAN
                                 └── manager_id → employee(id) [nullable]
```

Migrated with Flyway — all scripts in `src/main/resources/db/migration/`:

| Migration | What |
|-----------|------|
| `V43__create_employee.sql` | Employee table |
| `V44__create_intern.sql` | Intern table + FK |
| `V45`–`V46` | Manager nullable tweaks |
| `V47__insert_sample_data.sql` | 5 employees + 13 interns |

---

## 🚀 Local Dev

```bash
# 1. Start PostgreSQL
docker run -d -p 5432:5432 \
  -e POSTGRES_DB=employeedb \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=secret \
  postgres:13.9

# 2. Run the API
./gradlew bootRun
```

```env
# application.properties values (env vars)
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/employeedb
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=secret
FRONT_URL=http://localhost:5173
```
---

<div align="center">

**Built with [POJA](https://poja.io) ❤️**

</div>
