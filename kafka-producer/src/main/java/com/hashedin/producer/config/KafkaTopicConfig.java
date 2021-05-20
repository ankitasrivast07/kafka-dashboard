package com.hashedin.producer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Order(1)
public class KafkaTopicConfig {

    @Value("${kafka.producer.bootstrap-server}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String,Object> adminConfig = new HashMap<>();
        adminConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        return new KafkaAdmin(adminConfig);
    }
    @Bean
    public NewTopic arrivals(){
        return TopicBuilder
                .name("org.station.arrivals")
                .partitions(3)
                .replicas(3)
                .build();
    }
    @Bean
    public NewTopic turnstiles(){
        return TopicBuilder
                .name("org.station.turnstiles")
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic transformStations(){
        return TopicBuilder
                .name("org.station.transformstations")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic stations(){
        return TopicBuilder
                .name("org.station.stations")
                .partitions(3)
                .replicas(3)
                .build();
    }
    @Bean
    public NewTopic weather(){
        return TopicBuilder
                .name("org.station.weather")
                .partitions(3)
                .replicas(3)
                .build();
    }

}
