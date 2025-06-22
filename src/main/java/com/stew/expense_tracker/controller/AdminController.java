package com.stew.expense_tracker.controller;

import com.stew.expense_tracker.model.User;
import com.stew.expense_tracker.service.AdminService;
import com.stew.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@RequestBody User user) {
        return adminService.createAdmin(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAllUsers() {
        return adminService.findAllUsers();
    }

    @PutMapping("/set-role-admin/{userId}")
    public ResponseEntity<String> setRole(@PathVariable Integer userId) {
        return adminService.setRole(userId);
    }

}
