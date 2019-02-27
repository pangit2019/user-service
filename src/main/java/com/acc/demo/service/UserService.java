package com.acc.demo.service;

import com.acc.demo.model.User;

import java.util.List;

public interface UserService {

    void addUser(User user);

    void updateUser(User user);

    User findUser(String id);

    void deleteUser(String id);

    List<User> findAllUser();

    long countUsers();
}
