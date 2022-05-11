package com.rabbitmq.example.rabbitmqdemo.config;

import com.rabbitmq.example.rabbitmqdemo.exception.RabbitMQCustomErrorHandler;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.util.ErrorHandler;

@Configuration
public class RabbitMQConfiguration {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.virtualhost}")
    private String virtualhost;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    ConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(this.host, this.port);
        cachingConnectionFactory.setVirtualHost(this.virtualhost);
        cachingConnectionFactory.setUsername(this.username);
        cachingConnectionFactory.setPassword(this.password);
        return cachingConnectionFactory;
    }

    @Bean("usuarioFactory")
    public SimpleRabbitListenerContainerFactory usuarioFactory(){
        return this.simpleRabbitListenerContainerFactory(new SimpleRabbitListenerContainerFactory());
    }

    private SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactory factory) {
        factory.setBatchListener(false);
        factory.setConsumerBatchEnabled(false);
        factory.setDefaultRequeueRejected(false);
        factory.setConnectionFactory(connectionFactory());
        factory.setAdviceChain(retries());
        factory.setErrorHandler(errorHandller());
        return factory;
    }

    @Bean
    public ErrorHandler errorHandller() {
        return new RabbitMQCustomErrorHandler();
    }

    @Bean
    public RetryOperationsInterceptor retries() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(5000, 2, 20000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }
}
