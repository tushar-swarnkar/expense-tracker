package com.stew.expense_tracker.wrapper;

import com.stew.expense_tracker.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Month;

@Data
@RequiredArgsConstructor
public class ExpenseWrapper {

    private Integer amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private Month month;

    private int year;

}
