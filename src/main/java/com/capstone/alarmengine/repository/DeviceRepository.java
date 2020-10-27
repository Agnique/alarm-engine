package com.capstone.alarmengine.repository;

import com.capstone.alarmengine.model.Device;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.stream.Stream;

@RepositoryRestResource(collectionResourceRel = "Device", path = "Device")
public interface DeviceRepository extends Neo4jRepository<Device, Long> {

    @Query("MATCH (device {name: $0}) RETURN device")
    Device getDeviceByName(String name);

    Device findByName(String name);

    Stream<Device> findAllBy();

    @Query("MATCH (d: Device) RETURN d")
    List<Device> findAllDevices();


}
