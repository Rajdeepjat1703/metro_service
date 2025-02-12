package com.webknot.metro_service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic sosTopic() {
        return TopicBuilder.name("sos-topic").build();
    }
}
