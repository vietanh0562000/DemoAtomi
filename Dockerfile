# ====== BUILD STAGE ======
FROM gradle:8.10-jdk17 AS builder

WORKDIR /build

COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon || true

COPY . .

RUN gradle bootJar --no-daemon

# ====== RUN STAGE ======
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /build/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
