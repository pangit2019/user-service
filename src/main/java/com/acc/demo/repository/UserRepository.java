package com.acc.demo.repository;

import com.acc.demo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByIdIgnoreCase(String id);
}
