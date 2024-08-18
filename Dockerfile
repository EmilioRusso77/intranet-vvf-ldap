# Usa un'immagine di base con JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Imposta il working directory
WORKDIR /app

# Copia il file JAR generato dall'applicazione Spring Boot
# Utilizza un pattern generico per trovare l'ultima versione del JAR
COPY target/*.jar /app/ldap-service.jar

# Esponi la porta su cui l'applicazione ascolta
EXPOSE 8080

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "/app/ldap-service.jar"]

