FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /build

COPY pom.xml .
COPY src ./src
COPY checkstyle.xml .

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN useradd -m -u 1000 appuser

COPY --from=builder /build/target/*.jar app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher health || exit 1

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
