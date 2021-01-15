package com.pamungkasaji.beshop.controller;

import com.pamungkasaji.beshop.dto.UserDto;
import com.pamungkasaji.beshop.exceptions.UserServiceException;
import com.pamungkasaji.beshop.model.request.user.UserDetailRequest;
import com.pamungkasaji.beshop.model.response.OperationStatusModel;
import com.pamungkasaji.beshop.model.response.RequestOperationStatus;
import com.pamungkasaji.beshop.model.response.user.UserDetailResponse;
import com.pamungkasaji.beshop.service.UserService;
import com.pamungkasaji.beshop.shared.Roles;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostAuthorize("hasRole('ADMIN') or returnObject.userId == principal.userId")
    @GetMapping(path = "/{id}")
    public UserDetailResponse getUser(@PathVariable String id) {
        UserDetailResponse returnValue = new UserDetailResponse();

        UserDto userDto = userService.getUserByUserId(id);

        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userDto, UserDetailResponse.class);

        return returnValue;
    }

    @PostMapping
    public UserDetailResponse createUser(@RequestBody UserDetailRequest userDetailRequest) throws Exception{
        UserDetailResponse returnValue = new UserDetailResponse();

//        if(userDetailRequest.getName().isEmpty())
//            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetailRequest, userDto);

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = new ModelMapper().map(userDetailRequest, UserDto.class);
        userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserDetailResponse.class);

        return returnValue;
    }

    @PutMapping(path = "/{id}")
    public UserDetailResponse updateUser(@PathVariable String id, @RequestBody UserDetailRequest userDetailRequest) {
        UserDetailResponse returnValue = new UserDetailResponse();

//        if(userDetailRequest.getName().isEmpty())
//            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new ModelMapper().map(userDetailRequest, UserDto.class);

        UserDto updatedUser = userService.updateUser(id, userDto);
        returnValue = new ModelMapper().map(updatedUser, UserDetailResponse.class);

        return returnValue;
    }

    @GetMapping
    public List<UserDetailResponse> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<UserDetailResponse> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

		for (UserDto userDto : users) {
			UserDetailResponse userModel = new UserDetailResponse();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}

        return returnValue;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
//    @PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
//    @Secured("ROLE_ADMIN")
    @DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }
}
