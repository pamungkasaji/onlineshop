package com.pamungkasaji.beshop.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name="authorities")
public class Authority implements Serializable {

    private static final long serialVersionUID = -317513521642247794L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable=false)
    private String name;

    @ManyToMany(mappedBy="authorities")
    private Collection<Role> roles;

    public Authority() {}

    public Authority(String name) {
        this.name = name;
    }
}
