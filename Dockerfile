FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/food-atlas*.jar food-atlas.jar
ENTRYPOINT ["java", "-jar", "food-atlas.jar"]