# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /workspace/app
COPY pom.xml .
COPY src src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-jammy
LABEL maintainer="Arun <arun4.duraisamy@gmail.com>"

# Create a non-root user
RUN useradd -r -u 1001 -g root springuser

WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /workspace/app/target/*.jar app.jar

# Set permissions
RUN chown springuser:root /app \
    && chmod 755 /app \
    && chown springuser:root app.jar \
    && chmod 544 app.jar

# Configure JVM options for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Use non-root user
USER 1001

# Set healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Spring specific options
ENV SPRING_PROFILES_ACTIVE="default"

EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]