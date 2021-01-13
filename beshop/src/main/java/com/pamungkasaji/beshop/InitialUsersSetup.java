package com.pamungkasaji.beshop;

import com.pamungkasaji.beshop.entity.AuthorityEntity;
import com.pamungkasaji.beshop.entity.RoleEntity;
import com.pamungkasaji.beshop.entity.UserEntity;
import com.pamungkasaji.beshop.repository.AuthorityRepository;
import com.pamungkasaji.beshop.repository.RoleRepository;
import com.pamungkasaji.beshop.repository.UserRepository;
import com.pamungkasaji.beshop.shared.Roles;
import com.pamungkasaji.beshop.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

@Component
public class InitialUsersSetup {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("From Application ready event...");

        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority,writeAuthority));
        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority,writeAuthority, deleteAuthority));

        if(roleAdmin == null) return;

        UserEntity adminUser = new UserEntity();
        adminUser.setName("Aji Pamungkas");
        adminUser.setEmail("admin@test.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateId(30));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("123456"));
        adminUser.setRoles(Arrays.asList(roleAdmin));

        UserEntity storedUserDetails = userRepository.findByEmail("admin@test.com");
        if (storedUserDetails == null) {
            userRepository.save(adminUser);
        }
    }

    @Transactional
    private AuthorityEntity createAuthority(String name) {

        AuthorityEntity authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {

        RoleEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
