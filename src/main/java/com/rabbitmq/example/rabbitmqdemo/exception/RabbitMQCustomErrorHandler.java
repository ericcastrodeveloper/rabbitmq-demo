package com.rabbitmq.example.rabbitmqdemo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.util.ErrorHandler;

@Slf4j
public class RabbitMQCustomErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        log.error("Erro ao processar mensagem {}", t.getMessage());
        throw new AmqpRejectAndDontRequeueException("Erro ao processar mensagem!", t);
    }
}
