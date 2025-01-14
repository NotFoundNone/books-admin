package dev.admin.books.books_service.service;

import dev.admin.books.books_service.entity.BookEntity;
import dev.admin.books.books_service.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // GET all books (синхронно)
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    // GET single book (синхронно)
    public BookEntity getBookById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    // CREATE (асинхронно, вызывается из RabbitListener)
    public BookEntity createBook(BookEntity book) {
        return bookRepository.save(book);
    }

    // UPDATE (асинхронно)
    public BookEntity updateBook(BookEntity book) {
        return bookRepository.save(book);
    }

    // DELETE (асинхронно)
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}
