FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /workspace/target/cinemaclub-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

