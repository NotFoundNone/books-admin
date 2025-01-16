package dev.admin.books.books_service;

import io.grpc.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooksServiceApplication implements CommandLineRunner {

	@Autowired
	private Server grpcServer;

	public static void main(String[] args) {
		SpringApplication.run(BooksServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Запуск gRPC сервера на порту 8080...");
		grpcServer.start();
		grpcServer.awaitTermination();
	}

}
