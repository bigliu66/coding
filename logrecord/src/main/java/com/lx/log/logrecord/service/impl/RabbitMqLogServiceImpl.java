package com.lx.log.logrecord.service.impl;

import com.alibaba.fastjson.JSON;
import com.lx.log.logrecord.configuration.LogRecordProperties;
import com.lx.log.logrecord.model.LogDTO;
import com.lx.log.logrecord.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableConfigurationProperties({LogRecordProperties.class})
public class RabbitMqLogServiceImpl implements LogService {

    @Autowired
    private RabbitTemplate rubeExchangeTemplate;

    @Autowired
    private LogRecordProperties properties;

    @Override
    public boolean createLog(LogDTO logDTO) {
        log.info("LogRecord RabbitMq ready to send routingKey [{}] LogDTO [{}]", properties.getRabbitMqProperties().getRoutingKey(), logDTO);
        // 消息队列处理逻辑
        rubeExchangeTemplate.convertAndSend(properties.getRabbitMqProperties().getRoutingKey(), JSON.toJSONString(logDTO));
        return true;
    }
}
