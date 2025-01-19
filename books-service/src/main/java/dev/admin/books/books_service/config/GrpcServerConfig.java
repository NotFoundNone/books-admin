package dev.admin.books.books_service.config;

import dev.admin.books.books_service.service.BookGrpcService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Autowired
    private BookGrpcService bookGrpcService;

    //TODO: вынести в енвайронмент
    @Bean
    public Server grpcServer() throws Exception {
        return ServerBuilder.forPort(9091)
                .addService(bookGrpcService)
                .build();
    }
}