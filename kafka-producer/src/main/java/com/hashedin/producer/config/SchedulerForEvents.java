package com.hashedin.producer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hashedin.producer.model.TrainArrival;
import com.hashedin.producer.model.TurnStileEvent;
import com.hashedin.producer.producer.AllEventsProducer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@Log4j2
@EnableScheduling
public class SchedulerForEvents {

    @Autowired
    private AllEventsProducer allEventsProducer;

    private String stationsFilePath = "data/stations.csv";
    private static Map<Long,String> stationsDetailsMap = new HashMap<>();
    private static char[] directions = {'W','E','N','S'};
    private static String[] lines = {"red","green","blue"};
    private static String[] trainStatus = {"RUNNING","LATE","STOPPED"};
    private static String trainsArrivalTopicName = "org.station.arrivals";
    private static String  turnStilesTopicName = "org.station.turnstiles";

    @Autowired
    private ObjectMapper objectMapper;


    @PostConstruct
    public void getAllCsvData(){
        try{
            BufferedReader csvReader = new BufferedReader(
                    new InputStreamReader(getClass().getClassLoader().getResourceAsStream(stationsFilePath))
            );
            String row=null;
            int rowNo=0;
            while((row= csvReader.readLine())!=null){
                String[] data = row.split( ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if(rowNo!=0){
                    stationsDetailsMap.put(Long.valueOf(data[5]),data[4]);
                }
                rowNo++;
            }
        }
        catch (Exception ex){
            log.error("Error Occurs is =["+ex.getMessage()+"]");
        }
    }

    @Scheduled(fixedDelay =  300000) //5 minutes in milliseconds
    public void generateRandomTrainArrivalEvent(){
        try{
            Integer randomIndex = ThreadLocalRandom.current().nextInt(stationsDetailsMap.size());
            Integer prevRandomIndex = ThreadLocalRandom.current().nextInt(stationsDetailsMap.size());
            Long randomTrainId = ThreadLocalRandom.current().nextLong(10000,99999);
            List<Long> keyAsArray = new ArrayList<>(stationsDetailsMap.keySet());
            TrainArrival trainArrival = new TrainArrival();
            trainArrival.setStation_id(keyAsArray.get(randomIndex));
            trainArrival.setTrain_id(randomTrainId);
            trainArrival.setDirection(directions[ThreadLocalRandom.current().nextInt(4)]);
            trainArrival.setLine(lines[ThreadLocalRandom.current().nextInt(3)]);
            trainArrival.setTrain_status(trainStatus[ThreadLocalRandom.current().nextInt(3)]);
            trainArrival.setPrev_station_id(keyAsArray.get(prevRandomIndex));
            trainArrival.setPrev_direction(directions[ThreadLocalRandom.current().nextInt(4)]);
            allEventsProducer.sendEventProduced(trainsArrivalTopicName, trainArrival);
            log.info("Train Arrived with Details {}", trainArrival);
        }
        catch (Exception ex){
            log.error("Error Occurs is = "+ex.getMessage());
        }
    }
    @Scheduled(fixedDelay = 3000*60)
    public void generateRandomTurnStileEvent(){
        try{
            Integer randomIndex = ThreadLocalRandom.current().nextInt(stationsDetailsMap.size());
            List<Long> keyAsArray = new ArrayList<>(stationsDetailsMap.keySet());
            TurnStileEvent turnStileEvent = new TurnStileEvent();
            turnStileEvent.setStation_id(keyAsArray.get(randomIndex));
            turnStileEvent.setStation_name(stationsDetailsMap.get(keyAsArray.get(randomIndex)));
            turnStileEvent.setLine(lines[ThreadLocalRandom.current().nextInt(3)]);
            allEventsProducer.sendEventProduced(turnStilesTopicName, turnStileEvent);
            log.info("Person Entered with  Details {}",turnStileEvent);
        }
        catch (Exception ex){
            log.error("Error Occurs is = "+ex.getMessage());
        }
    }



}
