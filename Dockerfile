# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Gradle wrapper and build files first (better layer caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew

# Download dependencies (cached layer if build files unchanged)
RUN ./gradlew dependencies --no-daemon || true

# Copy source and build the jar (skip tests here; CI already ran them)
COPY src src
RUN ./gradlew clean bootJar --no-daemon -x test

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Non-root user
RUN addgroup -S spring && adduser -S spring -G spring
RUN apk add --no-cache curl

COPY --from=build /app/build/libs/*.jar app.jar

RUN chown spring:spring app.jar
USER spring

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
