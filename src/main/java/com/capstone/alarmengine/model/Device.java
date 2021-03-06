package com.capstone.alarmengine.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;
import java.util.stream.Collectors;

@NodeEntity
public class Device {
    @Id @GeneratedValue private Long id;

    private static final HashMap<String, String> substation_map = new HashMap<>() {
        {
            put("High_Voltage", "High_Voltage");
            put("Sources", "High_Voltage");
            put("Medium_Voltage", "Medium_Voltage");
            put("Low_Voltage", "Low_Voltage");
        }
    };

    private String name;

    private String substation = ""; //High_Voltage, Medium_Voltage, Low_Voltage

    private Double Ia;    // Current A
    private Double Ib;    // Current B
    private Double Ic;    // Current B
    private Double Vab;    // Voltage A-B
    private Double Vbc;    // Voltage B-C
    private Double Vca;    // Voltage C-A

    private Boolean bkrOpen; // Is Breaker Open or Closed

    private Boolean isTripped; //Is Breaker Tripped

    private Boolean ConnectedToEnergizedSource; // Is Connected To Energized Source

    public Device(String name) {
        this.name = name;
        setSubstation(name);
        isTripped = true;
        bkrOpen = true;
        Ia = .0;
        Ib = .0;
        Ic = .0;
        Vab = .0;
        Vbc = .0;
        Vca = .0;
    }

    public Device() {
        name = "";
        isTripped = true;
        bkrOpen = true;
        Ia = .0;
        Ib = .0;
        Ic = .0;
        Vab = .0;
        Vbc = .0;
        Vca = .0;
    }

    public Boolean getIsTripped(){
        return isTripped;
    }

    public Boolean getBkrOpen() {
        return bkrOpen;
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

    public String getSubstation() {
        return substation;
    }

    public void setSubstation(String name) {
        String[] subNames = name.split("\\.");
        for(String subName: subNames){
            if(substation_map.containsKey(subName)){
                this.substation = substation_map.get(subName);
                break;
            }
        }
    }

    public void setIsTripped(Boolean isTripped){
        this.isTripped = isTripped;
    }

    public void setBkrOpen(Boolean bkrOpen) {
        this.bkrOpen = bkrOpen;
    }

    public void setIa(Double Ia) {
        this.Ia = Ia;
    }

    public void setIb(Double Ib) {
        this.Ib = Ib;
    }

    public void setIc(Double Ic) {
        this.Ic = Ic;
    }

    public void setVab(Double Vab) {
        this.Vab = Vab;
    }

    public void setVbc(Double Vbc) {
        this.Vbc = Vbc;
    }

    public void setVca(Double Vca) {
        this.Vca = Vca;
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

    public Set<Device> getTransmitDevices() {
        return transmitDevices;
    }

    public void setTransmitDevices(Set<Device> transmitDevices) {
        this.transmitDevices = transmitDevices;
    }

    @Relationship(type = "TRANSMIT", direction = Relationship.OUTGOING)
    public Set<Device> transmitDevices;

    public void transmit(Device device) {
        if (transmitDevices == null) {
            transmitDevices = new HashSet<>();
        }
        transmitDevices.add(device);
    }

    public void removeTransmit(Device device) {
        if (device == null) return;
        transmitDevices.remove(device);
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
