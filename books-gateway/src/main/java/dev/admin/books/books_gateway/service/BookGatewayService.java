package dev.admin.books.books_gateway.service;

import dev.admin.books.books_gateway.dto.BookDto;
import dev.admin.books.books_gateway.producer.BookProducer;
import dev.admin.books.books_gateway.util.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookGatewayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookGatewayService.class);

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
        // Пытаемся прочитать из кэша
        List<BookDto> cached = cacheService.getAllBooksFromCache();
        if (cached != null && !cached.isEmpty()) {
            LOGGER.info("All books gets from cache");
            return cached;
        }

        // Иначе дергаем gRPC
        List<BookDto> books = bookGrpcClient.getAllBooks();
        LOGGER.info("All books gets from database");
        // Кладём в кэш
        cacheService.cacheAllBooks(books);
        return books;
    }

    public BookDto getBookById(String id) {
        // Сначала проверяем в кэше
        BookDto cached = cacheService.getBookFromCache(id);
        if (cached != null) {
            LOGGER.info("Book with id = {} gets from cache", id);
            return cached;
        }

        // Иначе дергаем gRPC
        BookDto book = bookGrpcClient.getBookById(id);
        LOGGER.info("Book with id = {} gets from database", id);

        // Если нашли, кладём в кэш
        if (book != null && book.getId() != null && !book.getId().isEmpty()) {
            cacheService.cacheBook(book);
        }
        return book;
    }

    public void createBookAsync(BookDto dto) {
        bookProducer.sendCreateBook(BookMapper.toBookResponse(dto));
        // Можно очистить/инвалидировать кэш getAllBooks
        cacheService.evictAllBooksCache();
    }

    public void updateBookAsync(BookDto dto) {
        bookProducer.sendUpdateBook(BookMapper.toBookResponse(dto));
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
