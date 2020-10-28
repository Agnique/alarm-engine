package com.capstone.alarmengine.control;

import com.capstone.alarmengine.service.PullDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/data")
public class PullDataController {
    @Autowired
    PullDataService pullDataService;

    @GetMapping("/{id}")
    public String getDataPoint(@PathVariable String id) {
        pullDataService.validateCredentials();
        return pullDataService.getTagData(Arrays.asList(id));
    }
}
