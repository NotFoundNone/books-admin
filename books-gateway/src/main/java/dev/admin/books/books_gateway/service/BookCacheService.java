package dev.admin.books.books_gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.admin.books.books_gateway.dto.BookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BookCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookCacheService.class);

    private static final String BOOK_CACHE_KEY_PREFIX = "book_";
    private static final String ALL_BOOKS_KEY = "all_books";

    private final RedisTemplate<String, Object> redisTemplate;

    public BookCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheAllBooks(List<BookDto> books) {
        try {
            redisTemplate.opsForValue().set(ALL_BOOKS_KEY, books, 1, TimeUnit.MINUTES);
        } catch (Exception e) {
            LOGGER.warn("Failed to cache all books: {}", e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<BookDto> getAllBooksFromCache() {
        try {
            Object obj = redisTemplate.opsForValue().get(ALL_BOOKS_KEY);
            if (obj instanceof List) {
                return (List<BookDto>) obj;
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to get all books from cache: {}", e.getMessage(), e);
        }
        return null;
    }

    public void cacheBook(BookDto dto) {
        if (dto.getId() == null) return;
        String cacheKey = BOOK_CACHE_KEY_PREFIX + dto.getId();
        try {
            redisTemplate.opsForValue().set(cacheKey, dto, 1, TimeUnit.MINUTES);
        } catch (Exception e) {
            LOGGER.warn("Failed to cache book with ID {}: {}", dto.getId(), e.getMessage(), e);
        }
    }

    public BookDto getBookFromCache(String id) {
        String cacheKey = BOOK_CACHE_KEY_PREFIX + id;
        try {
            Object obj = redisTemplate.opsForValue().get(cacheKey);
            if (obj instanceof LinkedHashMap) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.convertValue(obj, BookDto.class);
            }
            if (obj instanceof BookDto) {
                return (BookDto) obj;
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to get book with ID {} from cache: {}", id, e.getMessage(), e);
        }
        return null;
    }

    public void evictBookCache(String id) {
        String cacheKey = BOOK_CACHE_KEY_PREFIX + id;
        try {
            redisTemplate.delete(cacheKey);
        } catch (Exception e) {
            LOGGER.warn("Failed to evict cache for book with ID {}: {}", id, e.getMessage(), e);
        }
    }

    public void evictAllBooksCache() {
        try {
            redisTemplate.delete(ALL_BOOKS_KEY);
        } catch (Exception e) {
            LOGGER.warn("Failed to evict all books cache: {}", e.getMessage(), e);
        }
    }
}
