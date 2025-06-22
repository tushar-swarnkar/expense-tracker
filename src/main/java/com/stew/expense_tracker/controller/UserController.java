package com.stew.expense_tracker.controller;

import com.stew.expense_tracker.model.Expense;
import com.stew.expense_tracker.model.User;
import com.stew.expense_tracker.service.UserService;
import com.stew.expense_tracker.wrapper.ExpenseWrapper;
import com.stew.expense_tracker.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        return userService.signup(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserWrapper user) {
        return userService.login(user);
    }

    @PostMapping("/add-expense")
    public ResponseEntity<ExpenseWrapper> addExpense(@RequestBody Expense expense) {
        return userService.addExpense(expense);
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseWrapper>> getAllExpenses() {
        return userService.getAllExpenses();
    }

    @GetMapping("/expenses/{month}")
    public ResponseEntity<List<Expense>> getExpenseByMonth(@PathVariable String month) {
        return userService.getExpenseByMonth(month);
    }

    @GetMapping("/expenses/year/{year}/month/{month}")
    public ResponseEntity<List<Expense>> getExpenseByYearByMonth(@PathVariable("year") String year, @PathVariable("month") String month) {
        return userService.getExpenseByYearByMonth(year, month);
    }

    @GetMapping("/total/month/{month}/")
    public ResponseEntity<String> totalExpenditureMonth(@PathVariable String month) {
        return userService.expensesByMonth(month);
    }

    @GetMapping("/total/year/{year}/")
    public ResponseEntity<String> totalExpenditureMonth(@PathVariable Integer year) {
        return userService.expensesByYear(year);
    }

}
