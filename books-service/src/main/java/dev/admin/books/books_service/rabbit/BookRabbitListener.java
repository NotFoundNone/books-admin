package dev.admin.books.books_service.rabbit;

import com.example.bookproto.BookResponse;
import com.example.bookproto.DeleteBookMessage;
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
    public void handleCreateBook(BookResponse book) {
        BookEntity entity = new BookEntity(
            null,
            book.getTitle(),
            book.getAuthor(),
            book.getGenre(),
            book.getYear()
        );
        bookService.createBook(entity);
    }

    @RabbitListener(queues = "updateBookQueue")
    public void handleUpdateBook(BookResponse book) {
        if (book.getId() == null || book.getId().isEmpty()) {
            return;
        }
        BookEntity entity = new BookEntity(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getYear()
        );
        bookService.updateBook(entity);
    }

    @RabbitListener(queues = "deleteBookQueue")
    public void handleDeleteBook(DeleteBookMessage deleteMessage) {
        if (deleteMessage != null) {
            bookService.deleteBook(deleteMessage.getId());
        }
    }
}
