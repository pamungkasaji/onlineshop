package com.pamungkasaji.beshop.file;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class FileAttachment {

    @Id
    @GeneratedValue
    private long id;

    private String image;

    private String fileType;
}
