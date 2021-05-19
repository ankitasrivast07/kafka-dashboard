package com.hashedin.producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hashedin.producer.model.Stations;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Log4j2
public class StationsProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void addStationsData(String topicName, Stations stationsData) throws JsonProcessingException {
        String recordValue = objectMapper.writeValueAsString(stationsData);
        ListenableFuture<SendResult<String , String>> future =
                kafkaTemplate.send(topicName, recordValue);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleFailure( recordValue, throwable);
            }
            @Override
            public void onSuccess(SendResult<String, String> integerStringSendResult) {
                handleSuccess( recordValue, integerStringSendResult);
            }
        });
    }
    private void handleFailure(String value, Throwable ex) {
        log.error("Error and exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error  OnFailure: {}", throwable.getMessage());
        }
    }
    private void handleSuccess(String value, SendResult<String, String> result) {
        log.info("Message Sent SuccessFully for the  value is {} , partition is {}", value, result.getRecordMetadata().partition());
    }

}
