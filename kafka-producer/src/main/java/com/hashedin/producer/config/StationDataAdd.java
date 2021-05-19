package com.hashedin.producer.config;

import com.hashedin.producer.entity.AllStationsData;
import com.hashedin.producer.model.TransformStationData;
import com.hashedin.producer.producer.StationsProducer;
import com.hashedin.producer.repository.StationDataRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@Log4j2
@Order(3)
public class StationDataAdd {

    private static String  stationsTopicName = "org.station.stations";

    private String transformTopicName = "org.stations.transformstations";

    @Autowired
    private StationDataRepository repository;

    @Autowired
    private StationsProducer stationsAddProducer;

    @PostConstruct
    public void addAllStationsDataFromDatabaseToTopic(){
        try{
            List<AllStationsData> stationsDataList = repository.findAll();
            for(AllStationsData stationsData : stationsDataList){
                stationsAddProducer.addStationsData(stationsTopicName, stationsData);

                TransformStationData translatedData = new TransformStationData();
                translatedData.setStation_id(stationsData.getStation_id());
                translatedData.setStation_name(stationsData.getStation_name());
                translatedData.setOrder(stationsData.getOrder());
                boolean red = Boolean.parseBoolean(stationsData.getRed());
                boolean blue = Boolean.parseBoolean(stationsData.getBlue());
                boolean green =Boolean.parseBoolean(stationsData.getGreen());
                if(red){
                    translatedData.setLine("red");
                }
                else if(blue){
                    translatedData.setLine("blue");
                }
                else{
                    translatedData.setLine("green");
                }
                stationsAddProducer.addStationsData(transformTopicName, translatedData);
            }
        }
        catch (Exception ex){
            log.error("Error Occurs is =["+ex.getMessage()+"]");
        }
    }
}
