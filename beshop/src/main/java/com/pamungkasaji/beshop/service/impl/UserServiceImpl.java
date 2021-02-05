package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.dto.UserDto;
import com.pamungkasaji.beshop.entity.Role;
import com.pamungkasaji.beshop.entity.User;
import com.pamungkasaji.beshop.exceptions.UserServiceException;
import com.pamungkasaji.beshop.repository.RoleRepository;
import com.pamungkasaji.beshop.repository.UserRepository;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.UserService;
import com.pamungkasaji.beshop.shared.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    Utils utils;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null)
            throw new RuntimeException("Username already exists");

        User user = new User();

        BeanUtils.copyProperties(userDto, user);
//        ModelMapper modelMapper = new ModelMapper();
//        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        user.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        // Set roles
        Collection<Role> roleEntities = new HashSet<>();
        for(String role: userDto.getRoles()) {
            Role roleEntity = roleRepository.findByName(role);
            if(roleEntity !=null) {
                roleEntities.add(roleEntity);
            }
        }

        user.setRoles(roleEntities);

        User storedUserDetails = userRepository.save(user);
//        UserDto returnValue  = modelMapper.map(storedUserDetails, UserDto.class);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        // generate token after signup, belum bisa
//        String token = Jwts.builder()
//                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret() )
//                .compact();
//
//        returnValue.setToken(token);

        return returnValue;
    }

    @Override
    public UserDto getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(user, returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        User user = userRepository.findByUserId(userId);

        if (user == null) throw new UsernameNotFoundException("User with ID: " + userId + " not found");

        BeanUtils.copyProperties(user, returnValue);

        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnValue = new UserDto();

        User user = userRepository.findByUserId(userId);

        if (user == null)
            throw new UserServiceException(HttpStatus.NOT_FOUND);

        user.setName(userDto.getName());
        User updatedUserDetails = userRepository.save(user);
        returnValue = new ModelMapper().map(updatedUserDetails, UserDto.class);

        return returnValue;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();

        if(page>0) page = page-1;

        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<User> usersPage = userRepository.findAll(pageableRequest);
        List<User> users = usersPage.getContent();

        for (User user : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Transactional
    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId);

        if (user == null)
            throw new UserServiceException(HttpStatus.NOT_FOUND);

        userRepository.delete(user);
    }

    //this method will be used in the process of user sign in
    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) throw new UsernameNotFoundException(username);

        return new UserPrincipal(user);

//        return new User(user.getUsername(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return new UserPrincipal(user);
    }
}
