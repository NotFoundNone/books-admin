package dev.admin.books.books_gateway.controller;


import dev.admin.books.books_gateway.dto.BookDto;
import dev.admin.books.books_gateway.service.BookGatewayService;
import io.grpc.StatusRuntimeException;
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

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        try {
            List<BookDto> books = bookGatewayService.getAllBooks();
            return ResponseEntity.ok(books);
        }
        catch (StatusRuntimeException e)
        {
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable String id) {
        BookDto book = bookGatewayService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody BookDto dto) {
        bookGatewayService.createBookAsync(dto);
        return ResponseEntity.accepted().body("Creating book asynchronously...");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable String id, @RequestBody BookDto dto) {
        dto.setId(id); // Присваиваем ID из пути
        bookGatewayService.updateBookAsync(dto);
        return ResponseEntity.accepted().body("Updating book asynchronously...");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) {
        bookGatewayService.deleteBookAsync(id);
        return ResponseEntity.accepted().body("Deleting book asynchronously...");
    }
}
