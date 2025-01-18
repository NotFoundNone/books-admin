package dev.admin.books.books_service.util;

import com.example.bookproto.BookResponse;
import com.google.protobuf.Message;
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
        return new org.springframework.amqp.core.Message(bytes, messageProperties);
    }

    @Override
    public Object fromMessage(org.springframework.amqp.core.Message message) throws MessageConversionException {
        try {
            String contentType = message.getMessageProperties().getContentType();
            if (!"application/x-protobuf".equals(contentType)) {
                throw new MessageConversionException("Unsupported content type: " + contentType);
            }
            return BookResponse.parseFrom(message.getBody());
            
        } catch (Exception e) {
            throw new MessageConversionException("Failed to convert Protobuf message", e);
        }
    }
}
