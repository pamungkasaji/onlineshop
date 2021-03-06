package com.pamungkasaji.beshop.file;

import com.pamungkasaji.beshop.configuration.AppConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileService {

    AppConfiguration appConfiguration;

    Tika tika;

    FileAttachmentRepository fileAttachmentRepository;

    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public FileService(AppConfiguration appConfiguration, FileAttachmentRepository fileAttachmentRepository) {
        super();
        this.appConfiguration = appConfiguration;
        tika = new Tika();
        this.fileAttachmentRepository = fileAttachmentRepository;
    }

    public String saveProfileImage(String base64Image) throws IOException {
        // convert Base64 in byte[]
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

        // generate name of document
        String imageName = getRandomName();
        // cr8 target file + name of document
        File target = new File(appConfiguration.getFullProfileImagesPath() + "/" + imageName);

        // write byte to this file
        FileUtils.writeByteArrayToFile(target, decodedBytes);
        return imageName;
    }

    public String detectType(byte[] fileArr) {
        return tika.detect(fileArr);
    }

    public void deleteProfileImage(String image) {
        try {
            Files.deleteIfExists(Paths.get(appConfiguration.getFullProfileImagesPath() + "/" + image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileAttachment saveAttachment(MultipartFile file) {
        FileAttachment fileAttachment = new FileAttachment();

        try {
            // convert file to byte and detect file type
            byte[] fileAsByte = file.getBytes();
            String fileType = detectType(fileAsByte);
            String[] fileTypeSplit = fileType.split("/");

            String filename = getRandomName() + "." + fileTypeSplit[1];

            // save file to folder
            File target = new File(appConfiguration.getFullAttachmentsPath() + "/" + filename);
            // save the byte in target location
            FileUtils.writeByteArrayToFile(target, fileAsByte);

            fileAttachment.setFileType(fileType);
            fileAttachment.setImage("/images/" + appConfiguration.getAttachmentsFolder() + "/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileAttachmentRepository.save(fileAttachment);
    }

    //    @Scheduled(fixedRate = 1000 * 60 * 60)
//    public void cleanupStorage() {
//        Date oneHourAgo = new Date(System.currentTimeMillis() - (60 * 60 * 1000));
//        List<FileAttachment> oldFiles = fileAttachmentRepository.findByDateBeforeAndProductIsNull(oneHourAgo);
//        for (FileAttachment file : oldFiles) {
//            deleteAttachmentImage(file.getName());
//            fileAttachmentRepository.deleteById(file.getId());
//        }
//    }

    public void deleteAttachmentImage(String image) {
        try {
            Files.deleteIfExists(Paths.get(appConfiguration.getFullAttachmentsPath() + "/" + image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

