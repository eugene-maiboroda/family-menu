FROM --platform=linux/amd64 maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml ./
RUN mvn -X dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM --platform=linux/amd64 eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/familymenu-backend*jar familymenu-backend.jar
EXPOSE 8082
CMD ["java", "-jar", "familymenu-backend.jar"]
