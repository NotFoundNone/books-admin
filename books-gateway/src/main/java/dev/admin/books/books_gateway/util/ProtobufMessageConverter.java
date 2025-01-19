package dev.admin.books.books_gateway.util;

import com.google.protobuf.Message;
import dev.admin.books.BookResponse;
import dev.admin.books.DeleteBookMessage;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

public class ProtobufMessageConverter implements MessageConverter {

    @Override
    public org.springframework.amqp.core.Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        if (!(object instanceof Message)) {
            throw new MessageConversionException("Object is not a protobuf Message");
        }
        Message protobufMessage = (Message) object;
        byte[] bytes = protobufMessage.toByteArray();
        messageProperties.setContentType("application/x-protobuf");
        messageProperties.setContentLength(bytes.length);
        messageProperties.setHeader("protobuf-type", protobufMessage.getClass().getSimpleName());
        return new org.springframework.amqp.core.Message(bytes, messageProperties);
    }

    @Override
    public Object fromMessage(org.springframework.amqp.core.Message message) throws MessageConversionException {
        try {
            String contentType = message.getMessageProperties().getContentType();
            if (!"application/x-protobuf".equals(contentType)) {
                throw new MessageConversionException("Unsupported content type: " + contentType);
            }

            String protobufType = (String) message.getMessageProperties().getHeaders().get("protobuf-type");

            if ("BookResponse".equals(protobufType)) {
                return BookResponse.parseFrom(message.getBody());
            } else if ("DeleteBookMessage".equals(protobufType)) {
                return DeleteBookMessage.parseFrom(message.getBody());
            } else {
                throw new MessageConversionException("Unknown protobuf type: " + protobufType);
            }

        } catch (Exception e) {
            throw new MessageConversionException("Failed to convert Protobuf message", e);
        }
    }
}
