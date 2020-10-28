package com.capstone.alarmengine.control;

import com.capstone.alarmengine.service.PullDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class PullDataController {
    @Autowired
    PullDataService pullDataService;

    @GetMapping("")
    public String getData() {
        pullDataService.validateCredentials();
        return pullDataService.getTagData();
    }
}
