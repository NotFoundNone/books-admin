package dev.admin.books.books_gateway.config;

import dev.admin.books.books_gateway.util.ProtobufMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new ProtobufMessageConverter());
        return template;
    }

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
