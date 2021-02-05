package com.pamungkasaji.beshop;

import com.pamungkasaji.beshop.entity.AuthorityEntity;
import com.pamungkasaji.beshop.entity.ProductEntity;
import com.pamungkasaji.beshop.entity.RoleEntity;
import com.pamungkasaji.beshop.entity.UserEntity;
import com.pamungkasaji.beshop.file.FileAttachment;
import com.pamungkasaji.beshop.file.FileAttachmentRepository;
import com.pamungkasaji.beshop.repository.AuthorityRepository;
import com.pamungkasaji.beshop.repository.ProductRepository;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Component
public class InitialDataSetup {

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

    @Autowired
    ProductRepository productRepository;

    @Autowired
    FileAttachmentRepository fileAttachmentRepository;

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
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("123456"));
        adminUser.setRoles(Arrays.asList(roleAdmin));
        adminUser.setAdmin(true);

        UserEntity storedUserDetails = userRepository.findByEmail("admin@test.com");
        if (storedUserDetails == null) {
            userRepository.save(adminUser);
        }

//        UserEntity

        ProductEntity product1 = new ProductEntity();
        product1.setName("iPhone 11");
        product1.setBrand("Apple");
        product1.setDescription("The latest iPhone 11 128GB");
        product1.setPrice(new BigDecimal("900"));
        product1.setCountInStock(8);
        FileAttachment fileAttachment1 = new FileAttachment();
        fileAttachment1.setImage("/images/attachments/08f6dfcc94dc49ed8db67df540ff29ee.png");
        fileAttachment1.setFileType("image/png");
        fileAttachmentRepository.save(fileAttachment1);
        product1.setAttachment(fileAttachment1);
        productRepository.save(product1);

        ProductEntity product2 = new ProductEntity();
        product2.setName("PS5");
        product2.setBrand("Sony");
        product2.setDescription("The latest PS5 and Spiderman Miles Morales");
        product2.setPrice(new BigDecimal("500"));
        product2.setCountInStock(16);
        FileAttachment fileAttachment2 = new FileAttachment();
        fileAttachment2.setImage("/images/attachments/68a33c06fe374da6b6a57756df98e539.jpeg");
        fileAttachment2.setFileType("image/jpeg");
        fileAttachmentRepository.save(fileAttachment2);
        product2.setAttachment(fileAttachment2);
        productRepository.save(product2);
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
