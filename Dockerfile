# Use a base image with Java 25 (Early Access)
# Using a multi-stage build to keep the final image size small

# Build stage
FROM openjdk:25-ea-jdk-slim AS build
WORKDIR /app

# Copy Maven wrapper and project definition
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (this step is cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src src

# Build the application
# Skipping tests to speed up the build in this example, but recommended to run in CI
RUN ./mvnw clean package -DskipTests

# Run stage
FROM openjdk:25-ea-jdk-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
