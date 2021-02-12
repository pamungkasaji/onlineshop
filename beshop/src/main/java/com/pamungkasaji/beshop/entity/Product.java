package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pamungkasaji.beshop.file.FileAttachment;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "products")
@Data
public class Product implements Serializable {

    private static final long serialVersionUID = 6998426737153488264L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String productId;

    @NotNull
    private String name;

//    private String image;

    private String brand;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private int countInStock = 0;

    @OneToOne
    private FileAttachment attachment;

//    @ManyToOne
//    @JoinColumn(name="order_id")
//    private OrderEntity orderDetail;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
