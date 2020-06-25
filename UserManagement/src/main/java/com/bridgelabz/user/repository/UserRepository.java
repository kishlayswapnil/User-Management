package com.bridgelabz.user.repository;

import com.bridgelabz.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Interface extending the Jpa repository to access database values.
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmailId(String email);
}
