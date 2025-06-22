package com.stew.expense_tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Month;

@Entity
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private Month month;
    private int year;

    @JsonIgnore
    @ManyToOne
    private User user;
}

//    private String month = LocalDate.now().getMonth().toString();
//    private int year = LocalDate.now().getYear();

