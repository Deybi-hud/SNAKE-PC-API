# Etapa 1: Construir la aplicación, haciendo uso de Maven y Eclipse Temurin 21
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# Etapa 2: Crear la imagen final con la aplicación construida, usando Eclipse Temurin 21 JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]