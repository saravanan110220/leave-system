# Stage 1: Build Stage
FROM maven:3.9.0-eclipse-temurin-11 AS builder

WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime Stage
FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/target/leave-system.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
