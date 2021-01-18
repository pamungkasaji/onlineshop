package com.pamungkasaji.beshop.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name="users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 5286851583334434873L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(cascade= { CascadeType.PERSIST }, fetch = FetchType.EAGER )
    @JoinTable(name="users_roles",
            joinColumns=@JoinColumn(name="users_id",referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="roles_id",referencedColumnName="id"))
    private Collection<RoleEntity> roles;

    @Column(nullable=false)
    private String userId;

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
}
