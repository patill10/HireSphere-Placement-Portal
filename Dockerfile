# Step 1: Build the JAR file using Maven & Java 21
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .

# Grant execute permissions to Maven Wrapper
RUN chmod +x ./mvnw

# Build project skipping tests
RUN ./mvnw clean package -DskipTests

# Step 2: Create lightweight runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]