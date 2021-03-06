package com.lx.log.logrecord.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create rabbitmq queue exchange and bind by routingKey
 */
@Configuration
public class RabbitMQConfig {

    // 队列：logRecord-lx
    @Bean
    public Queue topicQueue() {
        return new Queue("logRecord-lx");
    }

    // 交换机：logRecord
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("logRecord");
    }

    // 绑定关系：logRecord-lx
    @Bean
    Binding bindingTopicExchange() {
        return BindingBuilder
                .bind(topicQueue())
                .to(topicExchange())
                .with("logRecord-lx");
    }

}
