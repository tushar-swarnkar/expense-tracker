package com.stew.expense_tracker.repository;

import com.stew.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
}
