package com.hashedin.producer.ktable;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

@Component
@EnableScheduling
@Log4j2
public class TurnstileTable {

    @Value("${kafka.producer.bootstrap-server}")
    private String bootstrapAddress;

    private static String  turnStilesTopicName = "org.station.turnstiles";

    //table Turnstiles in Kafka cluster from turnstile data provided
    @Scheduled(fixedDelay = 3000*60)
    public void generateTurnstilesKTable(){
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "turnstlies-data");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        //input topic
        KStream<String,String> inputTopic = builder.stream("org.station.turnstiles");
        KTable<String,String> topicData = inputTopic
                .toTable(Materialized.as("Turnstiles"));

    }
    //Table of Turnstiles_Summary
    @Scheduled(fixedDelay = 300000)
    public void generateTurnstiles(){
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "turnstiles-data");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String,String>  textLines = builder.stream("org.station.turnstiles");
        KTable<String,Long> wordsCount = textLines
                .flatMapValues(textLine -> Arrays.asList(textLine.split("\\W+")))
                .selectKey((key, word)->word)
                .groupByKey()
                .count(Named.as("Counts"));
    }

}

