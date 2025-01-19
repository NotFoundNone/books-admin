package dev.admin.books.books_gateway.service;


import dev.admin.books.*;
import dev.admin.books.books_gateway.dto.BookDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookGrpcClient {

    private BookServiceGrpc.BookServiceBlockingStub bookServiceStub;

    @Autowired
    public BookGrpcClient(BookServiceGrpc.BookServiceBlockingStub bookServiceStub) {
        this.bookServiceStub = bookServiceStub;
    }

    public List<BookDto> getAllBooks() {
        BookListResponse response = bookServiceStub.getAllBooks(EmptyRequest.getDefaultInstance());
        return response.getBooksList().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public BookDto getBookById(String id) {
        GetBookRequest request = GetBookRequest.newBuilder().setId(id).build();
        BookResponse response = bookServiceStub.getBookById(request);

        // Если вернулся пустой (id=""), считаем, что книга не найдена
        if (response.getId().isEmpty()) {
            return null;
        }
        return toDto(response);
    }

    private BookDto toDto(BookResponse r) {
        return new BookDto(
                r.getId(),
                r.getTitle(),
                r.getAuthor(),
                r.getGenre(),
                r.getYear()
        );
    }
}
