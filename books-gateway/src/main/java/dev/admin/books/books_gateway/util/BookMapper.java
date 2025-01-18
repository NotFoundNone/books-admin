package dev.admin.books.books_gateway.util;

import dev.admin.books.BookResponse;
import dev.admin.books.books_gateway.dto.BookDto;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public static BookResponse toBookResponse(BookDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("BookDto cannot be null");
        }

        return BookResponse.newBuilder()
                .setId(dto.getId())
                .setTitle(dto.getTitle())
                .setAuthor(dto.getAuthor())
                .setGenre(dto.getGenre())
                .setYear(dto.getYear())
                .build();
    }

    public static BookDto toBookDto(BookResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("BookResponse cannot be null");
        }

        BookDto dto = new BookDto();
        dto.setId(response.getId());
        dto.setTitle(response.getTitle());
        dto.setAuthor(response.getAuthor());
        dto.setGenre(response.getGenre());
        dto.setYear(response.getYear());
        return dto;
    }
}
