package dev.admin.books.books_service.rabbit;

import dev.admin.books.books_service.dto.BookDto;
import dev.admin.books.books_service.entity.BookEntity;
import dev.admin.books.books_service.service.BookService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookRabbitListener {

    private final BookService bookService;

    public BookRabbitListener(BookService bookService) {
        this.bookService = bookService;
    }

    @RabbitListener(queues = "createBookQueue")
    public void handleCreateBook(BookDto bookDto) {
        // При создании ID может не быть (Mongo сгенерирует автоматически)
        BookEntity entity = new BookEntity(
            null,
            bookDto.getTitle(),
            bookDto.getAuthor(),
            bookDto.getGenre(),
            bookDto.getYear()
        );
        bookService.createBook(entity);
    }

    @RabbitListener(queues = "updateBookQueue")
    public void handleUpdateBook(BookDto bookDto) {
        if (bookDto.getId() == null || bookDto.getId().isEmpty()) {
            // Логика, если не указан ID
            return;
        }
        BookEntity entity = new BookEntity(
            bookDto.getId(),
            bookDto.getTitle(),
            bookDto.getAuthor(),
            bookDto.getGenre(),
            bookDto.getYear()
        );
        bookService.updateBook(entity);
    }

    @RabbitListener(queues = "deleteBookQueue")
    public void handleDeleteBook(String id) {
        if (id != null && !id.isEmpty()) {
            bookService.deleteBook(id);
        }
    }
}
