package com.pamungkasaji.beshop.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {

//    List<FileAttachment> findByDateBeforeAndProductIsNull(Date date);
}
