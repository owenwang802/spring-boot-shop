package com.owen.springbootshop.dao;

import com.owen.springbootshop.dto.UserRegisterRequest;
import com.owen.springbootshop.model.User;

public interface UserDao {


        User getUserById(Integer id);

        Integer createUser(UserRegisterRequest userRegisterRequest);

        User getUserByEmail(String email);
}
