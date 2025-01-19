package dev.admin.books.books_service;

import io.grpc.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooksServiceApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(BooksServiceApplication.class);

	@Autowired
	private Server grpcServer;

	public static void main(String[] args) {
		SpringApplication.run(BooksServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		grpcServer.start();
		grpcServer.awaitTermination();
	}
}
