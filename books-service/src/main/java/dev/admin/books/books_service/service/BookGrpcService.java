package dev.admin.books.books_service.service;

import com.example.bookproto.*;
import dev.admin.books.books_service.entity.BookEntity;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class BookGrpcService extends BookServiceGrpc.BookServiceImplBase {

    private final BookService bookService;

    public BookGrpcService(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /books/{id}
    @Override
    public void getBookById(GetBookRequest request, StreamObserver<BookResponse> responseObserver) {
        String id = request.getId();
        BookEntity entity = bookService.getBookById(id);

        BookResponse response;
        if (entity != null) {
            response = BookResponse.newBuilder()
                    .setId(entity.getId())
                    .setTitle(entity.getTitle())
                    .setAuthor(entity.getAuthor())
                    .setGenre(entity.getGenre())
                    .setYear(entity.getYear())
                    .build();
        } else {
            // Возвращаем пустой BookResponse (или можно прокидывать ошибку через Status)
            response = BookResponse.newBuilder()
                    .setId("")
                    .setTitle("")
                    .setAuthor("")
                    .setGenre("")
                    .setYear(0)
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // GET /books
    @Override
    public void getAllBooks(EmptyRequest request, StreamObserver<BookListResponse> responseObserver) {
        List<BookEntity> allBooks = bookService.getAllBooks();

        BookListResponse.Builder builder = BookListResponse.newBuilder();
        for (BookEntity entity : allBooks) {
            BookResponse bookResp = BookResponse.newBuilder()
                    .setId(entity.getId())
                    .setTitle(entity.getTitle())
                    .setAuthor(entity.getAuthor())
                    .setGenre(entity.getGenre())
                    .setYear(entity.getYear())
                    .build();
            builder.addBooks(bookResp);
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}