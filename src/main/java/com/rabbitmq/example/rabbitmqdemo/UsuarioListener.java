package com.rabbitmq.example.rabbitmqdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsuarioListener {

    @RabbitListener(queues = "usuario", containerFactory = "usuarioFactory")
    public void subscribeMessageUserQueue(Message message){
        String messageString = new String(message.getBody());
        log.info("Mensagem recebida {}", messageString);
//        teste de dlq
        throw new RuntimeException("Erro ao receber mensagem");
    }
}
