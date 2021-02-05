package com.pamungkasaji.beshop;

import com.pamungkasaji.beshop.entity.Authority;
import com.pamungkasaji.beshop.entity.Product;
import com.pamungkasaji.beshop.entity.Role;
import com.pamungkasaji.beshop.entity.User;
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

        Authority readAuthority = createAuthority("READ_AUTHORITY");
        Authority writeAuthority = createAuthority("WRITE_AUTHORITY");
        Authority deleteAuthority = createAuthority("DELETE_AUTHORITY");

        createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority,writeAuthority));
        Role roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority,writeAuthority, deleteAuthority));

        if(roleAdmin == null) return;

        User adminUser = new User();
        adminUser.setName("Aji Pamungkas");
        adminUser.setUsername("akunadmin");
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("123456"));
        adminUser.setRoles(Arrays.asList(roleAdmin));
        adminUser.setAdmin(true);

        User storedUserDetails = userRepository.findByUsername("admin@test.com");
        if (storedUserDetails == null) {
            userRepository.save(adminUser);
        }

//        UserEntity

        Product product1 = new Product();
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

        Product product2 = new Product();
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
    private Authority createAuthority(String name) {

        Authority authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new Authority(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    private Role createRole(String name, Collection<Authority> authorities) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
