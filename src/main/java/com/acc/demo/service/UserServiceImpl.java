package com.acc.demo.service;

import com.acc.demo.model.User;
import com.acc.demo.repository.UserRepository;
import com.acc.demo.utils.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional
    public void addUser(User user) {
        if(StringUtils.isEmpty(user.getId())) {
            throw new UserServiceException("User id can not be null or empty.");
        }

        User dbUser = repository.findByIdIgnoreCase(user.getId());

        if (dbUser != null) {
            throw new UserServiceException("User already exists.");
        }

        repository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        User dbUser = repository.findByIdIgnoreCase(user.getId());

        if (dbUser == null) {
            throw new UserServiceException("User not found for id : " + user.getId());
        }

        dbUser.setFirstName(user.getFirstName());
        dbUser.setSurName(user.getSurName());
        repository.save(dbUser);
    }

    @Override
    @Transactional
    public User findUser(String id) {
        return repository.findByIdIgnoreCase(id);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserServiceException("User not found for id : " + id);
        }
    }

    @Override
    @Transactional
    public List<User> findAllUser() {
        return (List<User>) repository.findAll();
    }

    @Override
    public long countUsers() {
        return repository.count();
    }
}
