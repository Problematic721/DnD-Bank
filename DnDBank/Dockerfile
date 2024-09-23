FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/dndbank-0.0.1-SNAPSHOT.jar /app/dndbank.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "dndbank.jar"]