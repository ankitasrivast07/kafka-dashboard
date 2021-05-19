package com.hashedin.producer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hashedin.producer.model.WeatherEvent;
import com.hashedin.producer.producer.AllEventsProducer;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Configuration
@Log4j2
@EnableScheduling
public class WeatherApi {
    private static String weatherTopic = "org.station.weather";

    @Value("${weather.api.key}")
    private static String weatherApiKey = "695ac39adefd3a620bb53153d769911d";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AllEventsProducer allEventsProducer;


    @Scheduled(fixedDelay = 86400000)   //milliseconds in a day
    public void getWeatherDataEvent(){
        try{
            WeatherEvent weatherEvent = new WeatherEvent();
            StringBuilder apiBuilder = new StringBuilder();
            apiBuilder.append("http://api.openweathermap.org/data/2.5/weather?q=Lucknow&appid=")
                    .append(weatherApiKey)
                    .append("&units=metric");
            URL url = new URL(apiBuilder.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode != 200){
                throw  new RuntimeException("Api is not valid !!");
            }
            Scanner scanner = new Scanner(url.openStream());
            StringBuilder responseStream = new StringBuilder();
            while(scanner.hasNext()){
                String data = scanner.next();
                responseStream.append(data);
            }
            JSONObject response = new JSONObject(responseStream.toString());
            log.info(responseStream.toString());
            Double temp = response.getJSONObject("main").getDouble("temp");
            weatherEvent.setTemperature(temp);
            if(temp>=25){
                weatherEvent.setStatus("Sunny");
            }
            else if(temp>=20&&temp<25){
                weatherEvent.setStatus("Spring");
            }
            else{
                weatherEvent.setStatus("Winter");
            }
            allEventsProducer.sendEventProduced(weatherTopic, weatherEvent);
            log.info(" Weather Api running {}",weatherEvent);
        }
        catch (Exception ex){
            log.error("Error is = "+ex.getMessage());
        }
    }
}

