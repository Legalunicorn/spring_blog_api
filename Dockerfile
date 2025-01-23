FROM eclipse-temurin:17
COPY ./target/blog_api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

