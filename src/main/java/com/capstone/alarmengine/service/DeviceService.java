package com.capstone.alarmengine.service;

import com.capstone.alarmengine.model.Device;
import com.capstone.alarmengine.repository.DeviceRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private PullDataService pullDataService;

    public Device getDevice(String name) {
        return deviceRepository.findByName(name);
    }
    public Stream<Device> getAllDevices() {
        return deviceRepository.findAllBy();
    }
    public List<Device> getAllDownstreamDevices(String name) {return deviceRepository.findAllDownstreamDevices(name);}
    
    @Scheduled(fixedRate = 1000)
    void updateAllDevices() {
        pullDataService.login();
        pullDataService.getRealTimeToken();
        Stream<Device> devices = getAllDevices();
        devices.forEach(this::updateDevice);
    }

    void updateDevice(Device d) {
        String name = d.getName();
        if (d == null) return;
        String[] doubleTags = {"Ia", "Ib", "Ic", "Vab", "Vbc", "Vca"};
        String[] boolTags = {"BkrOpen"};
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
            Boolean value = valueStr == "0" ? true : false;
            switch (tag) {
                case "BkrOpen":
                    d.setBkrOpen(value);
                    break;
            }
        }
        deviceRepository.save(d);
    }

}
