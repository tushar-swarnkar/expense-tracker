package com.stew.expense_tracker.service;

import com.stew.expense_tracker.Constants.UserRoles;
import com.stew.expense_tracker.CustomExceptions.UserAlreadyExists;
import com.stew.expense_tracker.model.User;
import com.stew.expense_tracker.repository.UserDao;
import com.stew.expense_tracker.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminService {

    @Autowired
    UserDao userDao;

    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseEntity<String> setRole(Integer userId) {
        try {
            Optional<User> u = userDao.findById(userId);
            if (u.isPresent()) {
                List<UserRoles> roles = u.get().getRoles();
                if (!roles.contains(UserRoles.ADMIN)) {
                    roles.add(UserRoles.ADMIN);
                }
                u.get().setRoles(roles);
                userDao.save(u.get());
                return new ResponseEntity<>("User role updated successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User with user id " + userId + " does not exist", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error finding the user with id {}", userId, e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<User>> findAllUsers() {
        try {
            List<User> users = userDao.findAll();
            if (!users.isEmpty()) {
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error finding the list of users", e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> createAdmin(User user) {
        try {
            Optional<User> userFromDb = userDao.findByEmail(user.getEmail());
            if (userFromDb.isPresent()) {
                throw new UserAlreadyExists("User with email " + user.getEmail() + " already exists!!");
            }

            if (user.getName().isBlank() || Character.isDigit(user.getName().charAt(0))) {
                throw new IllegalArgumentException("Username cannot be empty!!");
            }
            if (user.getEmail().isBlank()) {
                throw new IllegalArgumentException("Email cannot be empty!!");
            }
            if (user.getPassword().isBlank()) {
                throw new IllegalArgumentException("Password cannot be empty!!");
            }

            user.setPassword(encoder.encode(user.getPassword()));
            user.setRoles(List.of(UserRoles.USER, UserRoles.ADMIN));
            userDao.save(user);

            return new ResponseEntity<>("Admin created!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error creating Admin for user id {}", user.getId(), e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
