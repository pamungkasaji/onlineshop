package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 5286851583334434873L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String userId;

    @ManyToMany(cascade= { CascadeType.PERSIST }, fetch = FetchType.EAGER )
    @JoinTable(name="users_roles",
            joinColumns=@JoinColumn(name="users_id",referencedColumnName="userId"),
            inverseJoinColumns=@JoinColumn(name="roles_id",referencedColumnName="id"))
    private Collection<RoleEntity> roles;

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable=false, length=120, unique = true)
    private String email;

    @Column(nullable=false)
    private String encryptedPassword;

    @Column(nullable=false)
    private boolean admin;

    private String token;

    private String emailVerificationToken;

    @Column(nullable=false)
    private Boolean emailVerificationStatus = false;

//    @OneToMany(mappedBy="userDetail", cascade=CascadeType.ALL)
//    private List<ProductEntity> orderItems;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
