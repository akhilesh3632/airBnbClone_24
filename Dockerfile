FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

COPY airBnbApp/pom.xml .

COPY airBnbApp/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]