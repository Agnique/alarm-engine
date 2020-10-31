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
    private Boolean BkrOpen; // Breaker Open
    private Double Ia;    // Current A
    private Double Ib;    // Current B
    private Double Ic;    // Current B
    private Double Vab;    // Voltage A-B
    private Double Vbc;    // Voltage B-C
    private Double Vca;    // Voltage C-A

    public Device(String name) {
        this.name = name;
    }

    public Device(String name, Set<Device> connectedDevices) {
        this.name = name;
        this.connectedDevices = connectedDevices;
    }


    public Boolean getBkrOpen() {
        return BkrOpen;
    }

    public Double getIa() {
        return Ia;
    }

    public Double getIb() {
        return Ib;
    }

    public Double getIc() {
        return Ic;
    }

    public Double getVab() {
        return Vab;
    }

    public Double getVbc() {
        return Vbc;
    }

    public Double getVca() {
        return Vca;
    }

    public void setBkrOpen(Boolean bkrOpen) {
        BkrOpen = bkrOpen;
    }

    public void setIa(Double ia) {
        this.Ia = ia;
    }

    public void setIb(Double ib) {
        this.Ib = ib;
    }

    public void setIc(Double ic) {
        this.Ic = ic;
    }

    public void setVab(Double vab) {
        Vab = vab;
    }

    public void setVbc(Double vbc) {
        Vbc = vbc;
    }

    public void setVca(Double vca) {
        Vca = vca;
    }

    public Device() {

    }

    public void setConnectedDevices(Set<Device> connectedDevices) {
        this.connectedDevices = connectedDevices;
    }

    @Relationship(type = "CONNECT", direction = Relationship.OUTGOING)
    public Set<Device> connectedDevices;

    public Set<Device> getConnectedDevices() {
        return connectedDevices;
    }

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
