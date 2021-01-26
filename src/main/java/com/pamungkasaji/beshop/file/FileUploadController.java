package com.pamungkasaji.beshop.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    FileService fileService;

    @PostMapping("/products/upload")
    FileAttachment uploadForProduct(MultipartFile file) {
        return fileService.saveAttachment(file);
    }

}
