package com.capstone.alarmengine.control;

import com.capstone.alarmengine.model.Device;
import com.capstone.alarmengine.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    DeviceService deviceService;

    @GetMapping("")
    public Stream<Device> list() {
        return deviceService.getAllDevices();
    }
    @GetMapping("/{name}")
    public ResponseEntity<Device> get(@PathVariable String name) {
        try {
            Device device = deviceService.getDevice(name);
            return new ResponseEntity<Device>(device, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<Device>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/downstream/{name}")
    public List<Device> getDownstreamDevices(@PathVariable String name) {
        return deviceService.getAllDownstreamDevices(name);
    }
}
