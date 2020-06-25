package com.reactive.spring.reactiveapi.repository;

import com.reactive.spring.reactiveapi.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,String> {

}
