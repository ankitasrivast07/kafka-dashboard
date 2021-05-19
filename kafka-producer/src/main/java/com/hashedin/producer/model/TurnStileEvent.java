package com.hashedin.producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TurnStileEvent implements  Event{
    private Long station_id;
    private String station_name;
    private String line;
}
