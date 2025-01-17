FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/books-service-0.0.1.jar domain-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "domain-service.jar"]
