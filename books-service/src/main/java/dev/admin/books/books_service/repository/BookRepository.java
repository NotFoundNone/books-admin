package dev.admin.books.books_service.repository;

import dev.admin.books.books_service.entity.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<BookEntity, String> {
}
