package dev.admin.books.books_gateway.service;

import dev.admin.books.books_gateway.dto.BookDto;
import dev.admin.books.books_gateway.producer.BookProducer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookGatewayService {

    private final BookGrpcClient bookGrpcClient;
    private final BookCacheService cacheService; // сервис для Redis кеша
    private final BookProducer bookProducer;     // сервис для отправки RabbitMQ

    public BookGatewayService(BookGrpcClient bookGrpcClient,
                              BookCacheService cacheService,
                              BookProducer bookProducer) {
        this.bookGrpcClient = bookGrpcClient;
        this.cacheService = cacheService;
        this.bookProducer = bookProducer;
    }

    public List<BookDto> getAllBooks() {
        // Пытаемся прочитать из кэша (опционально, если хотите кешировать getAllBooks)
        List<BookDto> cached = cacheService.getAllBooksFromCache();
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        // Иначе дергаем gRPC
        List<BookDto> books = bookGrpcClient.getAllBooks();
        // Кладём в кэш
        cacheService.cacheAllBooks(books);
        return books;
    }

    public BookDto getBookById(String id) {
        // Сначала проверяем в кэше
        BookDto cached = cacheService.getBookFromCache(id);
        if (cached != null) {
            return cached;
        }

        // Иначе дергаем gRPC
        BookDto book = bookGrpcClient.getBookById(id);

        // Если нашли, кладём в кэш
        if (book != null && book.getId() != null && !book.getId().isEmpty()) {
            cacheService.cacheBook(book);
        }
        return book;
    }

    public void createBookAsync(BookDto dto) {
        bookProducer.sendCreateBook(dto);
        // Можно очистить/инвалидировать кэш getAllBooks
        cacheService.evictAllBooksCache();
    }

    public void updateBookAsync(BookDto dto) {
        bookProducer.sendUpdateBook(dto);
        // Сбрасываем кэш конкретной книги и общего списка
        cacheService.evictBookCache(dto.getId());
        cacheService.evictAllBooksCache();
    }

    public void deleteBookAsync(String id) {
        bookProducer.sendDeleteBook(id);
        // Сбрасываем кэш
        cacheService.evictBookCache(id);
        cacheService.evictAllBooksCache();
    }
}
