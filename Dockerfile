# build stage
FROM eclipse-temurin:17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# package stage
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/blog_api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
