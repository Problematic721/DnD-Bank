FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
COPY --from=build /app/target/DnDBank-1.0.jar /app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "/app.jar"]