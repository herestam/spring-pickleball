# ---------- STAGE 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# copy pom first (cache dependencies)
COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline

# copy source
COPY src ./src

# build jar
RUN mvn clean package -DskipTests


# ---------- STAGE 2: Run ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# copy jar from builder
COPY --from=builder /app/target/identity-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
