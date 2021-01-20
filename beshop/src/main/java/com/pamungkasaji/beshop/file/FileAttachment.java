package com.pamungkasaji.beshop.file;

import com.pamungkasaji.beshop.entity.ProductEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class FileAttachment {

    @Id
    @GeneratedValue
    private long id;

    private String image;

    private String fileType;
}
