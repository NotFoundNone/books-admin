package dev.admin.books.books_service.util;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseChecker {

    private final MongoTemplate mongoTemplate;

    public DatabaseChecker(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void checkDatabaseConnection() {
        try {
            mongoTemplate.executeCommand("{ ping: 1 }");
        } catch (DataAccessResourceFailureException ex) {
            throw new StatusRuntimeException(
                    Status.UNAVAILABLE.withDescription("The database is currently unavailable.")
            );
        } catch (Exception ex) {
            throw new StatusRuntimeException(
                    Status.INTERNAL.withDescription("An unexpected error occurred.")
            );
        }
    }
}
