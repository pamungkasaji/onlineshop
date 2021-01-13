package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.dto.UserDto;
import com.pamungkasaji.beshop.entity.RoleEntity;
import com.pamungkasaji.beshop.entity.UserEntity;
import com.pamungkasaji.beshop.exceptions.UserServiceException;
import com.pamungkasaji.beshop.model.response.error.ErrorMessages;
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
        if (userRepository.findByEmail(userDto.getEmail()) != null)
            throw new RuntimeException("Email already exists");

        UserEntity userEntity = new UserEntity();

        BeanUtils.copyProperties(userDto, userEntity);
//        ModelMapper modelMapper = new ModelMapper();
//        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        String generateUserId = utils.generateId(25);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(generateUserId);

        // Set roles
        Collection<RoleEntity> roleEntities = new HashSet<>();
        for(String role: userDto.getRoles()) {
            RoleEntity roleEntity = roleRepository.findByName(role);
            if(roleEntity !=null) {
                roleEntities.add(roleEntity);
            }
        }

        userEntity.setRoles(roleEntities);

        UserEntity storedUserDetails = userRepository.save(userEntity);
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
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) throw new UsernameNotFoundException("User with ID: " + userId + " not found");

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnValue = new UserDto();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setName(userDto.getName());
        UserEntity updatedUserDetails = userRepository.save(userEntity);
        returnValue = new ModelMapper().map(updatedUserDetails, UserDto.class);

        return returnValue;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();

        if(page>0) page = page-1;

        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Transactional
    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);

    }

    //this method will be used in the process of user sign in
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new UserPrincipal(userEntity);

//        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return new UserPrincipal(userEntity);
    }
}
