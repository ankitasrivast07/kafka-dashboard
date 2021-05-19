package com.hashedin.producer.entity;

import com.hashedin.producer.model.Stations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stations")
public class AllStationsData  implements Stations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stop_id")
    private Integer stop_id;
    @Column(name = "direction_id",nullable = false)
    private String direction_id;
    @Column(name = "stop_name",nullable = false)
    private String stop_name;
    @Column(name = "station_name",nullable = false)
    private String station_name;
    @Column(name = "station_descriptive_name",nullable = false)
    private String station_descriptive_name;
    @Column(name = "station_id",nullable = false)
    private Long station_id;
    @Column(name = "\"order\"")
    private Integer order;
    @Column(name = "red",nullable = false)
    private String red;
    @Column(name = "green",nullable = false)
    private String green;
    @Column(name = "blue",nullable = false)
    private String blue;

}
