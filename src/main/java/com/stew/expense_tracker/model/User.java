package com.stew.expense_tracker.model;

import com.stew.expense_tracker.Constants.UserRoles;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<UserRoles> roles;

    @CollectionTable(name = "user_expenses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "expenses")
    @OneToMany
    private List<Expense> expenseList = new ArrayList<>();

}
