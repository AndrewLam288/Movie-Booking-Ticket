# Movie Booking Project

A full-stack movie booking platform designed with a scalable backend, a modern frontend, and real-time capabilities for seat availability and booking updates.

## Overview

This project is being built as a **Modular Monolith** guided by **Clean Architecture principles**.  
The goal is to create a maintainable, production-oriented foundation that can support core movie booking workflows such as movie discovery, showtime browsing, seat selection, and real-time booking updates.

## Tech Stack

### Frontend
- React
- TypeScript
- Vite

### Backend
- Java
- Spring Boot

### Database
- PostgreSQL

### Realtime
- Spring WebSocket

### Infrastructure
- Docker
- Docker Compose

### Local Cloud Simulation
- LocalStack *(planned for a later phase)*

## Architecture

The system is currently structured around the following architectural direction:

- **Modular Monolith**
    - single deployable backend application
    - clear separation by domain/module
    - easier development and operational simplicity in early and mid stages

- **Clean Architecture principles**
    - separation of domain, application, and infrastructure concerns
    - reduced coupling between layers
    - improved maintainability and testability

- **Configuration best practices**
    - environment-based configuration
    - no hard-coded credentials, ports, or service URLs
    - clean separation between local and future deployment settings

## Current Scope

The current project foundation is focused on establishing:

- frontend and backend application setup
- PostgreSQL integration
- Docker-based local database setup
- environment-driven configuration
- a clean starting point for future feature development

## Planned Core Features

The long-term project direction includes:

- movie catalog browsing
- movie detail pages
- theater and showtime browsing
- seat map display
- booking flow
- real-time seat availability updates
- booking history
- future infrastructure and deployment improvements

## Repository Structure

```text
movie-booking-project/
├── backend/                 # Spring Boot backend
├── frontend/                # React + TypeScript + Vite frontend
├── docker-compose.yml       # Local service orchestration
├── .env                     # Local environment variables (not committed)
└── README.md
```

> This structure may evolve as the project grows.  
> Additional directories such as `docs/`, `scripts/`, or infrastructure-specific folders may be introduced later.

## Configuration

This project follows an environment-based configuration approach.

Sensitive values and environment-specific settings should be provided through environment variables rather than hard-coded in source code.

Typical configuration values include:

- database host
- database port
- database name
- database username
- database password
- backend application port
- frontend API base URL

## Prerequisites

Make sure the following tools are installed locally:

- Node.js
- npm
- Java JDK
- Maven
- Docker
- Docker Compose

## Getting Started

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd movie-booking-project
```

### 2. Create a local environment file

Create a `.env` file in the project root.

Example:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=movie_booking
DB_USERNAME=postgres
DB_PASSWORD=your_password
BACKEND_PORT=8080
VITE_API_BASE_URL=http://localhost:8080
```

> The exact variable names may change as the project evolves.

### 3. Start local services

Start PostgreSQL with Docker Compose:

```bash
docker compose up -d
```

### 4. Run the backend

From the `backend` directory:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

### 5. Run the frontend

From the `frontend` directory:

```bash
npm install
npm run dev
```

## Development Status

### Current Progress

The project foundation currently includes:


### Next Planned Steps


## Engineering Principles

This project aims to follow these standards:



## Future Improvements



## Notes

This README is intentionally focused on the current foundation and architectural direction.  
It will be expanded as the project matures and additional modules and workflows are implemented.