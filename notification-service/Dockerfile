# Build this Dockerfile from the root project directory with:
# docker build -f notification-service/Dockerfile -t notification-service .

FROM maven:3.9.6-eclipse-temurin-21 AS build

# Build notification service
WORKDIR /app
COPY . /app/
RUN mvn clean package -DskipTests

# -----------------------------------
FROM eclipse-temurin:21-jdk
COPY --from=build /app/target/notification-service-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 9080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]