package com.owen.springbootshop.service;


import com.owen.springbootshop.dto.UserLoginRequest;
import com.owen.springbootshop.dto.UserRegisterRequest;
import com.owen.springbootshop.model.User;


public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer id);

    User login(UserLoginRequest userLoginRequest);
}
