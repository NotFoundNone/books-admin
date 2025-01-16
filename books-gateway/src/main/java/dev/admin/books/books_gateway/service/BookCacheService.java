package dev.admin.books.books_gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.admin.books.books_gateway.dto.BookDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BookCacheService {

    private static final String BOOK_CACHE_KEY_PREFIX = "book_";
    private static final String ALL_BOOKS_KEY = "all_books";

    private final RedisTemplate<String, Object> redisTemplate;

    public BookCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Кэшируем список всех книг
    public void cacheAllBooks(List<BookDto> books) {
        redisTemplate.opsForValue().set(ALL_BOOKS_KEY, books, 1, TimeUnit.MINUTES);
    }

    // Получить список книг из кэша
    @SuppressWarnings("unchecked")
    public List<BookDto> getAllBooksFromCache() {
        Object obj = redisTemplate.opsForValue().get(ALL_BOOKS_KEY);
        if (obj instanceof List) {
            return (List<BookDto>) obj;
        }
        return null;
    }

    // Кэшируем одну книгу
    public void cacheBook(BookDto dto) {
        if (dto.getId() == null) return;
        String cacheKey = BOOK_CACHE_KEY_PREFIX + dto.getId();
        redisTemplate.opsForValue().set(cacheKey, dto, 1, TimeUnit.MINUTES);
    }

    // Получить одну книгу
    public BookDto getBookFromCache(String id) {
        String cacheKey = BOOK_CACHE_KEY_PREFIX + id;
        Object obj = redisTemplate.opsForValue().get(cacheKey);
        if (obj instanceof LinkedHashMap) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(obj, BookDto.class);
        }
        if (obj instanceof BookDto) {
            return (BookDto) obj;
        }
        return null;
    }

    // Сбросить кеш одной книги
    public void evictBookCache(String id) {
        redisTemplate.delete(BOOK_CACHE_KEY_PREFIX + id);
    }

    // Сбросить кеш списка всех книг
    public void evictAllBooksCache() {
        redisTemplate.delete(ALL_BOOKS_KEY);
    }
}
