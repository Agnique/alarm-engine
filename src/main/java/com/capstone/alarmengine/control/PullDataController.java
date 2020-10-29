package com.capstone.alarmengine.control;

import com.capstone.alarmengine.service.PullDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/data")
public class PullDataController {
    @Autowired
    PullDataService pullDataService;

    @GetMapping("/{id}")
    public String postRealTme(@PathVariable String id) {
        return pullDataService.postRealTimeData(Arrays.asList(id));
    }

    @GetMapping("/events")
    public String postEvents() {
        return pullDataService.postEventData();
    }

    @GetMapping("/alarms")
    public String postAlarms() {
        return pullDataService.postAlarmData();
    }

}
