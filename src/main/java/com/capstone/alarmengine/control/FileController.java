package com.capstone.alarmengine.control;

import com.capstone.alarmengine.service.DeviceService;
import com.capstone.alarmengine.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    DeviceService deviceService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file) {
        String filename = fileStorageService.storeFile(file);
        deviceService.updateModel();
        return "redirect:/";
    }


}
