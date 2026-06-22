# Build the React client
FROM node:20-alpine AS frontend-build

WORKDIR /app

COPY client/package*.json ./
RUN npm ci

COPY client/ .

ARG VITE_API_BASE_URL=/api/v1
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

RUN npm run build


# Build Spring Boot and package the React build inside it
FROM maven:3.9.9-eclipse-temurin-21 AS backend-build

WORKDIR /app

COPY server/pom.xml .
RUN mvn -B -q dependency:go-offline

COPY server/src ./src

# Spring Boot serves files in src/main/resources/static
COPY --from=frontend-build /app/dist ./src/main/resources/static

RUN mvn -B -DskipTests clean package


# Run one Java application on Render
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 10000

ENTRYPOINT ["sh", "-c", "exec java -jar /app/app.jar --server.port=${PORT:-10000}"]