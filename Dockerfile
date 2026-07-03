# ETAPA 1: Construir la aplicación
FROM eclipse-temurin:25-jdk-jammy AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew &&./gradlew dependencies --no-daemon   
COPY src src
RUN ./gradlew bootJar -x test
# ETAPA 2: Crear la imagen final ligera
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app

# Copiamos el jar desde la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
# Ejecutamos
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]