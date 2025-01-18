package dev.admin.books.books_service.config;

import dev.admin.books.books_service.util.ProtobufMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter protobufMessageConverter() {
        return new ProtobufMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter protobufMessageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(protobufMessageConverter);
        return factory;
    }

    @Bean
    public DirectExchange bookExchange() {
        return new DirectExchange("booksExchange");
    }

    // CREATE
    @Bean
    public Queue createBookQueue() {
        return new Queue("createBookQueue", true);
    }

    @Bean
    public Binding createBookBinding(Queue createBookQueue, DirectExchange bookExchange) {
        return BindingBuilder.bind(createBookQueue).to(bookExchange).with("book.create.key");
    }

    // UPDATE
    @Bean
    public Queue updateBookQueue() {
        return new Queue("updateBookQueue", true);
    }

    @Bean
    public Binding updateBookBinding(Queue updateBookQueue, DirectExchange bookExchange) {
        return BindingBuilder.bind(updateBookQueue).to(bookExchange).with("book.update.key");
    }

    // DELETE
    @Bean
    public Queue deleteBookQueue() {
        return new Queue("deleteBookQueue", true);
    }

    @Bean
    public Binding deleteBookBinding(Queue deleteBookQueue, DirectExchange bookExchange) {
        return BindingBuilder.bind(deleteBookQueue).to(bookExchange).with("book.delete.key");
    }
}
