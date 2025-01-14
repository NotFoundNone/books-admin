package dev.admin.books.books_gateway.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange
    @Bean
    public DirectExchange booksExchange() {
        return new DirectExchange("booksExchange");
    }

    // Queues
    @Bean
    public Queue createQueue() {
        return new Queue("createBookQueue");
    }

    @Bean
    public Queue updateQueue() {
        return new Queue("updateBookQueue");
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue("deleteBookQueue");
    }

    // Bindings
    @Bean
    public Binding createBinding(Queue createQueue, DirectExchange booksExchange) {
        return BindingBuilder.bind(createQueue).to(booksExchange).with("book.create.key");
    }

    @Bean
    public Binding updateBinding(Queue updateQueue, DirectExchange booksExchange) {
        return BindingBuilder.bind(updateQueue).to(booksExchange).with("book.update.key");
    }

    @Bean
    public Binding deleteBinding(Queue deleteQueue, DirectExchange booksExchange) {
        return BindingBuilder.bind(deleteQueue).to(booksExchange).with("book.delete.key");
    }
}
