package dev.admin.books.books_gateway.controller;


import dev.admin.books.books_gateway.dto.BookDto;
import dev.admin.books.books_gateway.service.BookGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookGatewayService bookGatewayService;

    public BookController(BookGatewayService bookGatewayService) {
        this.bookGatewayService = bookGatewayService;
    }

    // GET all books
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookGatewayService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // GET book by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable String id) {
        BookDto book = bookGatewayService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    // CREATE book
    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody BookDto dto) {
        bookGatewayService.createBookAsync(dto);
        return ResponseEntity.accepted().body("Creating book asynchronously...");
    }

    // UPDATE book
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable String id, @RequestBody BookDto dto) {
        dto.setId(id); // Присваиваем ID из пути
        bookGatewayService.updateBookAsync(dto);
        return ResponseEntity.accepted().body("Updating book asynchronously...");
    }

    // DELETE book
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) {
        bookGatewayService.deleteBookAsync(id);
        return ResponseEntity.accepted().body("Deleting book asynchronously...");
    }
}
