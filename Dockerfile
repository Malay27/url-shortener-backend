# 1. Use a base image with Java 17
FROM eclipse-temurin:17-jdk-alpine

# 2. Set a temp volume (helps Spring Boot)
VOLUME /tmp

# 3. Copy your app jar into the container and rename it
COPY target/URL-Shortening-Service-0.0.1-SNAPSHOT.jar app.jar

# 4. Port number
EXPOSE 8080

# 5. Command to run your app
ENTRYPOINT ["java","-jar","/app.jar"]
