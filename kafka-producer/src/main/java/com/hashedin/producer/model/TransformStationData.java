package com.hashedin.producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransformStationData implements Stations{
    private  Long station_id;
    private String station_name;
    private Integer order;
    private String line;
}
