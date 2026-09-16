# 🎬 CineBook

A backend REST API for a movie ticket booking platform, built with **Spring Boot 3.5.6** and **Java 21**. It supports managing movies, theatres, screens, and shows, along with seat-level booking, JWT-based authentication, and role-based access control (Admin, Theatre Manager, User).

## Features

- **Authentication & Authorization** — JWT-based signup/signin/signout with role-based access (`ROLE_ADMIN`, `ROLE_THEATRE_MANAGER`, `ROLE_USER`)
- **Role Management** — Admins can promote/demote users to/from Theatre Manager
- **Movie Management** — Add, update, delete, search, and paginate/sort movies; upload movie poster images
- **Theatre Management** — Create and manage theatres by name, city, and state, owned by a Theatre Manager
- **Screen Management** — Add and manage screens within a theatre
- **Show Management** — Schedule shows for a movie on a specific screen, with seat layouts and pricing
- **Seat-Level Booking** — View seat availability/layout per show, book seats, and cancel bookings
- **Booking History** — Retrieve a user's past bookings
- **API Docs** — Swagger/OpenAPI UI via springdoc
- **Global Exception Handling** — Centralized API error responses

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.6 (Web MVC, Data JPA, Security, Validation) |
| Database | MySQL |
| Auth | JWT (`jjwt` 0.13.0), Spring Security, BCrypt |
| API Docs | springdoc-openapi (Swagger UI) |
| Object Mapping | ModelMapper |
| Build Tool | Maven |
| Boilerplate Reduction | Lombok |

## Project Structure

```
src/main/java/com/moviebooking/project
├── config/            # App configuration & constants (pagination/sort defaults, etc.)
├── controller/         # REST controllers (Auth, Role, Movie, Theatre, Screen, Show, Booking)
├── exception/          # Custom exceptions & global exception handler
├── model/               # JPA entities (Movie, Theatre, Screen, Show, Seat, ShowSeat, Booking, User, Role, ...)
├── Payload/DTOs/         # Data transfer objects
├── Payload/Response/     # Paginated/list API responses (MovieResponse, TheatreResponse, ScreenResponse, ShowResponse)
├── Response/             # Seating layout response model
├── repository/          # Spring Data JPA repositories
├── request/              # Request payload models
├── security/             # Spring Security config, JWT utils/filters, auth services
├── services/              # Business logic layer
└── utils/                 # Utility helpers (AuthUtil, etc.)
```

## API Overview

Base path: `/api`

### Auth (`/api/auth`) — public unless noted
| Method | Endpoint | Description |
|---|---|---|
| POST | `/signin` | Log in and receive a JWT |
| POST | `/signup` | Register a new user |
| POST | `/signout` | Log out *(authenticated)* |
| GET | `/user` | Get current authenticated user *(USER, THEATRE_MANAGER, ADMIN)* |

### Roles (`/api/admin`) — ADMIN only
| Method | Endpoint | Description |
|---|---|---|
| POST | `/promoteUser/{userId}` | Promote a user to Theatre Manager |
| POST | `/demoteUser/{userId}` | Remove a user's Theatre Manager role |

### Movies
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/admin/movies` | Add a movie *(ADMIN)* |
| GET | `/api/public/movies` | List all movies (paginated/sortable) |
| GET | `/api/public/movies/{movieId}` | Get movie details |
| GET | `/api/public/movies/search?keyword=` | Search movies by keyword |
| PUT | `/api/admin/movies/{movieId}` | Update a movie *(ADMIN)* |
| DELETE | `/api/admin/movies/{movieId}` | Delete a movie *(ADMIN)* |
| PUT | `/api/admin/movies/{movieId}/image` | Upload/update movie poster *(ADMIN)* |

### Theatres
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/theatres` | Add a theatre *(THEATRE_MANAGER)* |
| GET | `/api/public/theatres` | List all theatres (paginated/sortable) |
| GET | `/api/public/theatres/{theatreId}` | Get theatre by ID |
| GET | `/api/public/theatres/name/{theatreName}` | Get theatre by name |
| GET | `/api/public/theatres/city/{city}` | List theatres by city |
| GET | `/api/public/theatres/state/{state}` | List theatres by state |
| PUT | `/api/theatres/{theatreId}` | Update a theatre *(THEATRE_MANAGER)* |
| DELETE | `/api/theatres/{theatreId}` | Delete a theatre *(THEATRE_MANAGER)* |

### Screens
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/theatres/{theatreId}/screens` | Add a screen to a theatre *(THEATRE_MANAGER)* |
| GET | `/api/public/screens/` | List all screens (paginated/sortable) |
| GET | `/api/public/theatres/{theatreId}/screens` | List screens by theatre |
| GET | `/api/public/screens/{screenId}` | Get screen by ID |
| PUT | `/api/screens/{screenId}` | Update a screen *(THEATRE_MANAGER)* |
| DELETE | `/api/screens/{screenId}` | Delete a screen *(THEATRE_MANAGER)* |

### Shows
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/screens/{screenId}/movies/{movieId}/shows/` | Schedule a show *(THEATRE_MANAGER)* |
| GET | `/api/public/theatres/{theatreId}/shows/` | List shows by theatre (paginated/sortable) |
| GET | `/api/public/movies/{movieId}/shows/` | List shows by movie (paginated/sortable) |
| GET | `/api/public/shows/{showId}` | Get show details |
| GET | `/api/public/shows/{showId}/seats` | Get seat layout/availability for a show |
| PUT | `/api/screens/{screenId}/shows/{showId}` | Update a show *(THEATRE_MANAGER)* |
| DELETE | `/api/shows/{showId}` | Delete a show *(THEATRE_MANAGER)* |

### Bookings
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/bookings` | Create a booking |
| GET | `/api/bookings/history` | Get booking history for the current user *(USER, ADMIN)* |
| DELETE | `/api/bookings/{bookingId}` | Cancel a booking *(USER)* |

All `/api/public/**` and `/api/auth/**` routes are open; everything else requires a valid JWT, and several routes additionally require a specific role as noted above.

## Getting Started

### Prerequisites
- Java 21+
- Maven (or use the included Maven Wrapper)
- MySQL Server running locally (or update the datasource URL for a remote instance)

### Database Setup

The project uses **MySQL**. Create a database before starting the app:

```sql
CREATE DATABASE moviebooking;
```

Schema is auto-managed via `spring.jpa.hibernate.ddl-auto=update`.

### Configuration

Key settings live in `src/main/resources/application.properties`:

- `spring.datasource.url` / `username` / `password` — MySQL connection details
- `spring.app.jwtSecret` — JWT signing secret
- `spring.app.jwtExpirationMs` — JWT expiration time (ms)
- `spring.ecom.app.jwtCookieName` — auth cookie name
- `project.image` — directory for uploaded movie poster images


### Run the Application

```bash
# Clone the repository
git clone https://github.com/aryan010201/Movie-Booking-System.git
cd Movie-Booking-System

# Run with Maven Wrapper
./mvnw spring-boot:run     # macOS/Linux
mvnw.cmd spring-boot:run   # Windows
```

The application starts on `http://localhost:8080` by default.

### API Documentation

Swagger UI is available once the app is running:

```
http://localhost:8080/swagger-ui.html
```

## Running Tests

```bash
./mvnw test
```

## License

No license has been specified for this repository. Consider adding one (e.g., MIT, Apache 2.0) if you intend for others to use or contribute to this project.
