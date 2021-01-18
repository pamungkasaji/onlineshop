package com.pamungkasaji.beshop.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "beshop")
@Data
public class AppConfiguration {

    String uploadPath;
    String profileImagesFolder = "profile";
    String attachmentsFolder = "attachments";

    public String getFullProfileImagesPath() {
        return this.uploadPath + "/" + profileImagesFolder;
    }

    public String getFullAttachmentsPath() {
        return this.uploadPath + "/" + attachmentsFolder;
    }
}

