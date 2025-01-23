FROM eclipse-temurin:17
WORKDIR /app
COPY pom.xml .
RUN mvn clean package -DskipTests
COPY target/blog_api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
