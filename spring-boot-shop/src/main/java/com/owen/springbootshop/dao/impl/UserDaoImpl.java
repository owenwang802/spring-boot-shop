package com.owen.springbootshop.dao.impl;


import com.owen.springbootshop.dao.UserDao;
import com.owen.springbootshop.dto.UserRegisterRequest;
import com.owen.springbootshop.model.User;
import com.owen.springbootshop.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User getUserById(Integer id) {

        String sql = "SELECT user_id, email, password, created_date, last_modified_date" +
                " FROM user WHERE user_id = :userId";

        Map<String, Object> params = new HashMap<>();

        params.put("userId", id);

        // UserRowMapper -> return users
        List<User> userList = namedParameterJdbcTemplate.query(sql, params, new UserRowMapper());

        if (!userList.isEmpty()) {
            return userList.get(0);
        } else {
            return null;
        }

    }

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {

        String sql = "INSERT INTO user(email, password, created_date, last_modified_date) " +
                " VALUES (:email, :password, :createdDate, :lastModifiedDate)";

        Map<String, Object> params = new HashMap<>();

        params.put("email", userRegisterRequest.getEmail());
        params.put("password", userRegisterRequest.getPassword());

        Date now = new Date();
        params.put("createdDate", now);
        params.put("lastModifiedDate", now);

        // 這是 Spring 提供的實作，用來接收「資料庫產生的主鍵值」
        KeyHolder keyHolder = new GeneratedKeyHolder();
        // MapSqlParameterSource 把原本的 Map<String, Object>（即 params）轉換為 Spring 可接受的參數型別
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);

        int userId = keyHolder.getKey().intValue();;

        return userId;
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date FROM user WHERE email = :email";
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        List<User> userList = namedParameterJdbcTemplate.query(sql, params, new UserRowMapper());
        if (!userList.isEmpty()) {
            return userList.get(0);
        }

        return null;
    }


}
