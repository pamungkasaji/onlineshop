package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pamungkasaji.beshop.file.FileAttachment;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "products")
@Data
public class ProductEntity implements Serializable {

    private static final long serialVersionUID = 6998426737153488264L;

    @JsonIgnore
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false)
    private String productId;

    @Column(nullable=false)
    private String name;

//    private String image;

    private String brand;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private BigDecimal price;

    @Column(nullable=false)
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
