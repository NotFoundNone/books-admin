package dev.admin.books.books_gateway.config;

import dev.admin.books.BookServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
    }

    @Bean
    public BookServiceGrpc.BookServiceBlockingStub bookServiceStub(ManagedChannel channel) {
        return BookServiceGrpc.newBlockingStub(channel);
    }
}
