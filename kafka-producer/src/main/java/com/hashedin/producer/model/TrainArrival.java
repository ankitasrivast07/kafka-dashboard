package com.hashedin.producer.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class TrainArrival implements  Event{
    private Long station_id;
    private Long train_id;
    private char direction;
    private String line;
    private String train_status;
    private Long prev_station_id;
    private char prev_direction;


}
