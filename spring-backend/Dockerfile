# Multi-stage build for Spring Boot (Java 17)

FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven wrapper + pom first to leverage Docker layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Download dependencies (cacheable layer)
RUN ./mvnw -q -DskipTests dependency:go-offline

# Copy source and build
COPY src/ src/
RUN ./mvnw -q -DskipTests package


FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

# Good defaults for containers
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
