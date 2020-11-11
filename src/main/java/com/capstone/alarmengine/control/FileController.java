package com.capstone.alarmengine.control;

import com.capstone.alarmengine.service.DeviceService;
import com.capstone.alarmengine.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "http://localhost:3000")
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
