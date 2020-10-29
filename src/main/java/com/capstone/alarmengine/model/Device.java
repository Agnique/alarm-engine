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
    private Float la;    // Current A
    private Float lb;    // Current B
    private Float lc;    // Current B
    private Float Vab;    // Voltage A-B
    private Float Vbc;    // Voltage B-C
    private Float Vca;    // Voltage C-A

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

    public Float getLa() {
        return la;
    }

    public Float getLb() {
        return lb;
    }

    public Float getLc() {
        return lc;
    }

    public Float getVab() {
        return Vab;
    }

    public Float getVbc() {
        return Vbc;
    }

    public Float getVca() {
        return Vca;
    }

    public void setBkrOpen(Boolean bkrOpen) {
        BkrOpen = bkrOpen;
    }

    public void setLa(Float la) {
        this.la = la;
    }

    public void setLb(Float lb) {
        this.lb = lb;
    }

    public void setLc(Float lc) {
        this.lc = lc;
    }

    public void setVab(Float vab) {
        Vab = vab;
    }

    public void setVbc(Float vbc) {
        Vbc = vbc;
    }

    public void setVca(Float vca) {
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
