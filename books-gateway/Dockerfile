FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/books-gateway-0.0.1.jar gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway.jar"]
