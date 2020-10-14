package com.capstone.alarmengine.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@NodeEntity
public class Device {
    @Id @GeneratedValue private Long id;

    private String name;

    public Device() {

    }
    public Device(String name) {
        this.name = name;
    }

    @Relationship(type = "CONNECT", direction = Relationship.OUTGOING)
    public Set<Device> connectedDevices;

    public void connect(Device device) {
        if (connectedDevices == null) {
            connectedDevices = new HashSet<>();
        }
        connectedDevices.add(device);
    }

    public String toString() {
        return this.name + "'s connected items => "
                + Optional.ofNullable(this.connectedDevices).orElse(
                        Collections.emptySet()).stream().map(Device::getName).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
