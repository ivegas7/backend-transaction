# Etapa de compilación
# FROM eclipse-temurin:17-jdk AS builder
FROM eclipse-temurin:23.0.1_11-jdk AS builder
WORKDIR /backend

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Copiar el archivo pom.xml y las fuentes
COPY pom.xml .
COPY src ./src

# Construir la aplicación (omitimos los tests)
RUN mvn clean package -DskipTests

# Etapa de producción
FROM eclipse-temurin:17-jre
WORKDIR /apbackendp

# Copiar el archivo JAR generado
COPY --from=builder /backend/target/*.jar backend.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "backend.jar"]
