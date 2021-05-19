package com.hashedin.producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherEvent implements  Event {
    private  Double temperature;
    private String status;
}

