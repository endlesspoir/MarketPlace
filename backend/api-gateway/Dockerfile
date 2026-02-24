FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY mvnw .

COPY .mvn .mvn

COPY pom.xml .

RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B


COPY src src


RUN ./mvnw clean package -DskipTests


RUN java -Djarmode=layertools -jar target/*.jar extract


FROM eclipse-temurin:21-jre-alpine AS runtime


RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app




COPY --from=builder /app/dependencies/ ./


COPY --from=builder /app/spring-boot-loader/ ./


COPY --from=builder /app/snapshot-dependencies/ ./


COPY --from=builder /app/application/ ./


USER appuser


HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1


EXPOSE 8080


ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+UseStringDeduplication \
               -Djava.security.egd=file:/dev/./urandom"



ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]