package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@Table(name="users")
public class User implements Serializable {

    private static final long serialVersionUID = 5286851583334434873L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String userId;

    @ManyToMany(cascade= { CascadeType.PERSIST }, fetch = FetchType.EAGER )
    @JoinTable(name="users_roles",
            joinColumns=@JoinColumn(name="users_id",referencedColumnName="userId"),
            inverseJoinColumns=@JoinColumn(name="roles_id",referencedColumnName="id"))
    private Collection<Role> roles;

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable=false, length=120, unique = true)
    private String username;

    @Column(nullable=false)
    private String encryptedPassword;

    @Column(nullable=false)
    private boolean admin;

    @Transient
    private String token;

//    @OneToMany(mappedBy="userDetail", cascade=CascadeType.ALL)
//    private List<ProductEntity> orderItems;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
