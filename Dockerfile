# Stage 1: Build Stage
FROM maven:3.8.1-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime Stage
FROM openjdk:11-jre-slim
WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/target/leave-system-1.0-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]