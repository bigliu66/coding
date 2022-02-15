package com.lx.log.logrecord.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "log-record")
public class LogRecordProperties {

    private RabbitMqProperties rabbitMqProperties;

    @Data
    public static class RabbitMqProperties {
        private String host;
        private int port;
        private String username;
        private String password;
        private String queueName;
        private String exchangeName;
        private String routingKey;
    }
}
