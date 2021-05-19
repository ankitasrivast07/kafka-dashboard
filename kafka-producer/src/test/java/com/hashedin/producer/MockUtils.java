package com.hashedin.producer;


import com.hashedin.producer.model.TrainArrival;

public class MockUtils {

    public static final Long STATION_ID = Long.valueOf(40020);
    public static final Long TRAIN_ID = Long.valueOf(12296);
    public static final String LINE = "blue";
    public static final String TRAIN_STATUS = "onTime";
    public static final Long PREV_STATION_ID = Long.valueOf(40010);


    private MockUtils() {
    }

    public static TrainArrival getMockTrainArrival() {
        return TrainArrival.builder()
                .station_id(STATION_ID)
                .train_id(TRAIN_ID)
                .line(LINE)
                .train_status(TRAIN_STATUS)
                .prev_station_id(PREV_STATION_ID)
                .build();
    }

}

