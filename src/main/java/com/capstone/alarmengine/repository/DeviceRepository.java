package com.capstone.alarmengine.repository;

import com.capstone.alarmengine.model.Device;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DeviceRepository extends Neo4jRepository<Device, Long> {
    Device getItemByName(String name);
}
