package com.capstone.alarmengine.service;

import com.capstone.alarmengine.model.Device;
import com.capstone.alarmengine.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    public Device getDevice(String name) {
        return deviceRepository.findByName(name);
    }
    public Stream<Device> getAllDevices() {
        return deviceRepository.findAllBy();
    }
}
