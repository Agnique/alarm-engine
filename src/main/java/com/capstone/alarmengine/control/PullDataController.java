package com.capstone.alarmengine.control;

import com.capstone.alarmengine.service.PullDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/data", method = RequestMethod.GET, produces = "application/json")
@CrossOrigin(origins = "http://localhost:3000")
public class PullDataController {
    @Autowired
    PullDataService pullDataService;

    @GetMapping("/{id}")
    public String postRealTme(@PathVariable String id) {
        pullDataService.login();
        pullDataService.getRealTimeToken();
        return pullDataService.postRealTimeData(Arrays.asList(id));
    }

    @GetMapping("/events")
    public String postEvents() {
        pullDataService.login();
        pullDataService.getAlarmToken();
        return pullDataService.postEventData();
    }

    @GetMapping("/alarms")
    public String postAlarms() {
        pullDataService.login();
        pullDataService.getAlarmToken();
        return pullDataService.postAlarmData();
    }

}
