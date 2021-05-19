package com.hashedin.producer.repository;


import com.hashedin.producer.entity.AllStationsData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationDataRepository extends JpaRepository<AllStationsData,Integer> {
}
