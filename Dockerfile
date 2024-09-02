# Use an OpenJDK base image
FROM openjdk:17-slim

# Set the working directory in the Docker container
WORKDIR /app

# Copy the locally built JAR file into the container
COPY target/*.jar /app/registry-spring-boot-application.jar

# Expose the port your application uses
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/registry-spring-boot-application.jar"]
