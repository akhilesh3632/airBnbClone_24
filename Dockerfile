# =========================
# Build Stage
# =========================
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven configuration
COPY airBnbApp/pom.xml .

# Copy source code
COPY airBnbApp/src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests


# =========================
# Run Stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the generated JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Spring Boot port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]