package com.pamungkasaji.beshop.security;

import com.pamungkasaji.beshop.entity.Authority;
import com.pamungkasaji.beshop.entity.Role;
import com.pamungkasaji.beshop.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = -6133163490061541444L;

    private User user;
    private String userId;
    private String token;
    private boolean isAdmin;

    public UserPrincipal(User user) {
        this.user = user;
        this.userId = user.getUserId();
        this.isAdmin = user.isAdmin();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<Authority> authorityEntities = new HashSet<>();

        // Get user Roles
        Collection<Role> roles = user.getRoles();

        if(roles == null) return authorities;

        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorityEntities.addAll(role.getAuthorities());
        });

        authorityEntities.forEach((authority) ->{
            authorities.add(new SimpleGrantedAuthority(authority.getName()));
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
