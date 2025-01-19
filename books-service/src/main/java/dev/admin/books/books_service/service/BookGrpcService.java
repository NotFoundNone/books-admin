package dev.admin.books.books_service.service;

import com.example.bookproto.*;
import dev.admin.books.books_service.entity.BookEntity;
import dev.admin.books.books_service.util.DatabaseChecker;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class BookGrpcService extends BookServiceGrpc.BookServiceImplBase {

    private final BookService bookService;
    private final DatabaseChecker databaseChecker;

    @Autowired
    public BookGrpcService(BookService bookService, DatabaseChecker databaseChecker) {
        this.bookService = bookService;
        this.databaseChecker = databaseChecker;
    }

    @Override
    public void getBookById(GetBookRequest request, StreamObserver<BookResponse> responseObserver) {
        try {
            databaseChecker.checkDatabaseConnection();

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


        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getAllBooks(EmptyRequest request, StreamObserver<BookListResponse> responseObserver) {

        try {
            databaseChecker.checkDatabaseConnection();

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
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        }
    }
}