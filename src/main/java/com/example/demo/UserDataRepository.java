package com.example.demo;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserDataRepository extends MongoRepository<UserData, Integer> {
    Optional<UserData> findById(int id);
}
