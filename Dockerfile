# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy only the POM file first to leverage Docker cache
COPY pom.xml .

# Download dependencies (this layer will be cached unless POM changes)
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app runs on (change if different)
EXPOSE 8080

# Set environment variables if needed (e.g., database connection)
# ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/yourdb
# ENV SPRING_DATASOURCE_USERNAME=user
# ENV SPRING_DATASOURCE_PASSWORD=pass

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Use the following commands to build and run the Docker container:
# 1. Place this Dockerfile in the root of your project (same directory as pom.xml)
# 2. Build the image with: docker build -t bora-estudar-back .
# 3. Run the container with: docker run -p 8080:8080 bora-estudar-back