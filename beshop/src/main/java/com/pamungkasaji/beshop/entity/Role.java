package com.pamungkasaji.beshop.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name="roles")
public class Role implements Serializable {

    private static final long serialVersionUID = -1755654181565528087L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(nullable=false, length=30)
    private String name;

    @ManyToMany(mappedBy="roles")
    private Collection<User> users;

    @ManyToMany(cascade= { CascadeType.PERSIST }, fetch = FetchType.EAGER )
    @JoinTable(name="roles_authorities",
            joinColumns=@JoinColumn(name="roles_id",referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="authorities_id",referencedColumnName="id"))
    private Collection<Authority> authorities;

    public Role() {

    }

    public Role(String name) {
        this.name = name;
    }
}
