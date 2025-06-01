package com.owen.springbootshop.service.impl;


import com.owen.springbootshop.dao.UserDao;
import com.owen.springbootshop.dto.UserLoginRequest;
import com.owen.springbootshop.dto.UserRegisterRequest;
import com.owen.springbootshop.model.User;
import com.owen.springbootshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的eamil
        String email = userRegisterRequest.getEmail();
        User user = userDao.getUserByEmail(email);
        if (user != null) {
            logger.warn("該email {} 已經被註冊", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 使用MD5
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);
        // 創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer id) {


        return userDao.getUserById(id);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {

        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        if (user == null) {
            logger.warn("該email {} 沒有被註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        if (user.getPassword().equals(hashedPassword)) {
            return user;
        } else {
            logger.warn("email {} 密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }


}
