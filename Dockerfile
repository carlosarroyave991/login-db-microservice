# Stage 1: Build de la aplicación con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el pom y las fuentes
COPY pom.xml .
COPY src ./src

# Empaquetamos la aplicación
RUN mvn clean package -DskipTests

# Stage 2: Imagen final para ejecutar la aplicación
FROM eclipse-temurin:21-jdk-jammy AS micrologin
WORKDIR /app

# Copiamos el JAR generado en el stage anterior
COPY --from=build /app/target/login-0.0.1-SNAPSHOT.jar app.jar

# Se expone el puerto definido en las properties
EXPOSE 8080

# Se establece el comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
