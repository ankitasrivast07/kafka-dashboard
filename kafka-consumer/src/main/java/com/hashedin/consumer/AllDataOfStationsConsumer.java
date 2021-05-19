package com.hashedin.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class AllDataOfStationsConsumer {

    @KafkaListener(topics = "org.station.stations",groupId = "stations")
    public void consumeStations(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        log.info("Station : {} ", consumerRecord );
    }

    @KafkaListener(topics = "org.station.transformstations",groupId = "transform-stations")
    public void consumesTransformStations(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        log.info("Transform Station : {} ", consumerRecord );
    }
    @KafkaListener(topics = "org.station.weather",groupId = "weather")
    public void consumeWeatherData(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        log.info("Weather Record : {} ", consumerRecord );
    }


}