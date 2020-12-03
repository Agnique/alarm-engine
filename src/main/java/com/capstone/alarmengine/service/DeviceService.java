package com.capstone.alarmengine.service;

import com.capstone.alarmengine.model.Device;
import com.capstone.alarmengine.repository.DeviceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private PullDataService pullDataService;

    Logger log = LoggerFactory.getLogger(DeviceService.class);

    public Device getDevice(String name) {
        return deviceRepository.findByName(name);
    }
    public Stream<Device> getAllDevices() {
        return deviceRepository.findAllBy();
    }
    public List<Device> getAllDownstreamDevices(String name) {return deviceRepository.findAllDownstreamDevices(name);}

    @Scheduled(fixedRate = 1000)
    @Transactional
    void updateAllDevices() {
        pullDataService.login();
        pullDataService.getRealTimeToken();
        Iterable<Device> devices = deviceRepository.findAll();
        for (Device device : devices) {
            updateDevice(device);
        }
        for (Device device : devices) {
            updateTransmission(device);
        }
    }

    void updateDevice(Device d) {

        if (d == null) return;
        String name = d.getName();
        String[] doubleTags = {"Ia", "Ib", "Ic", "Vab", "Vbc", "Vca"};
        String[] boolTags = {"BkrOpen","isTripped"};
        for (String tag : doubleTags) {
            String res = pullDataService.postRealTimeData(Arrays.asList(name+"."+tag));
            JSONObject jsob = new JSONObject(res);
            String valueStr = jsob.optJSONArray("Data").getJSONObject(0).optString("Value");
            if (valueStr.length() == 0) continue;
            Double value = Double.valueOf(valueStr);
            switch (tag) {
                case "Ia":
                    d.setIa(value);
                    break;
                case "Ib":
                    d.setIb(value);
                    break;
                case "Ic":
                    d.setIc(value);
                    break;
                case "Vab":
                    d.setVab(value);
                    break;
                case "Vbc":
                    d.setVbc(value);
                    break;
                case "Vca":
                    d.setVca(value);
                    break;
            }
        }
        for (String tag : boolTags) {
            String res = pullDataService.postRealTimeData(Arrays.asList(name+"."+tag));
            JSONObject jsob = new JSONObject(res);
            String valueStr = jsob.optJSONArray("Data").getJSONObject(0).optString("Value");
            if (valueStr.length() == 0) continue;
            Boolean value = valueStr.equals("0") ? false : true;
            switch (tag) {
                case "BkrOpen":
                    d.setBkrOpen(value);
                    break;
                case "isTripped":
                    d.setIsTripped(value);
                    break;
            }
        }
        d.setSubstation(name);
        deviceRepository.save(d);

    }

    @Transactional
    public void updateTransmission(Device d) {
        if (d == null || d.getConnectedDevices() == null) return;

        if (d.getBkrOpen() || d.getIsTripped()) {
            for (Device downstreamDevice : d.getConnectedDevices()) {
                deviceRepository.deleteTransmit(d.getName(), downstreamDevice.getName());
            }
            return;
        }
        for (Device downstreamDevice : d.getConnectedDevices()) {
            if (downstreamDevice.getBkrOpen() || downstreamDevice.getIsTripped()) {
                deviceRepository.deleteTransmit(d.getName(), downstreamDevice.getName());
            } else if (downstreamDevice.getIa() > 0 || downstreamDevice.getIb() > 0 || downstreamDevice.getIc() > 0) {
                deviceRepository.createTransmit(d.getName(), downstreamDevice.getName());
            }
        }
    }

    public void updateModel() {
        log.info("starting");
        deviceRepository.deleteAll();
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Device>> typeReference = new TypeReference<List<Device>>() {};

        try {
            File modelFile = new File("./uploads/model.json");
            InputStream inputStream = new FileInputStream(modelFile);
            List<Device> devices = mapper.readValue(inputStream, typeReference);
            for (Device device : devices) {
                log.info(device.getName());
                Device d1 = deviceRepository.getDeviceByName(device.getName());
                if (d1 == null) d1 = new Device(device.getName());
                Set<Device> neighbors = device.getConnectedDevices();
                for (Device nei : neighbors) {
                    Device d2 = deviceRepository.getDeviceByName(nei.getName());
                    if (d2 == null) d2 = nei;
                    d1.connect(d2);
                }
                log.info(d1.getName());
                deviceRepository.save(d1);
            }
            log.info("INITIAL: Model saved to database.");
        } catch (IOException e){
            log.info("INITIAL: Unable to save model: " + e.getMessage());
        }
    }

}
