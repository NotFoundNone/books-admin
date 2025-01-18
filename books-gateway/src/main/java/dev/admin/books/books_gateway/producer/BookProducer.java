package dev.admin.books.books_gateway.producer;

import dev.admin.books.BookResponse;
import dev.admin.books.DeleteBookMessage;
import dev.admin.books.books_gateway.dto.BookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookProducer.class);

    private final AmqpTemplate rabbitTemplate;

    @Value("${app.rabbit.exchange}")
    private String exchange;

    @Value("${app.rabbit.create-routing-key}")
    private String createRoutingKey;

    @Value("${app.rabbit.update-routing-key}")
    private String updateRoutingKey;

    @Value("${app.rabbit.delete-routing-key}")
    private String deleteRoutingKey;

    public BookProducer(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateBook(BookResponse dto) {
        LOGGER.info("Sending create message: {} to exchange: {} with routing key: {}", dto, exchange, createRoutingKey);
        rabbitTemplate.convertAndSend(exchange, createRoutingKey, dto);
        LOGGER.info("Message sent successfully to exchange: {} with routing key: {}", exchange, createRoutingKey);
    }

    public void sendUpdateBook(BookResponse dto) {
        LOGGER.info("Sending update message: {} to exchange: {} with routing key: {}", dto, exchange, updateRoutingKey);
        rabbitTemplate.convertAndSend(exchange, updateRoutingKey, dto);
        LOGGER.info("Message sent successfully to exchange: {} with routing key: {}", exchange, updateRoutingKey);
    }

    public void sendDeleteBook(String id) {
        LOGGER.info("Sending delete message for ID: {} to exchange: {} with routing key: {}", id, exchange, deleteRoutingKey);
        DeleteBookMessage deleteMessage = DeleteBookMessage.newBuilder()
                .setId(id)
                .build();
        rabbitTemplate.convertAndSend(exchange, deleteRoutingKey, deleteMessage);
        LOGGER.info("Message sent successfully to exchange: {} with routing key: {}", exchange, deleteRoutingKey);
    }
}
