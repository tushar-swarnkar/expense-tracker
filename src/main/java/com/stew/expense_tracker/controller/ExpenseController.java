package com.stew.expense_tracker.controller;

import com.stew.expense_tracker.model.Expense;
import com.stew.expense_tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/all")
    public ResponseEntity<List<Expense>> getAllExpense() {
        return expenseService.getAllExpense();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Integer id) {
        return expenseService.getExpenseById(id);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<Expense>> getExpensesByYear(@PathVariable Integer year) {
        return expenseService.getExpensesByYear(year);
    }

    @GetMapping("/month/{month}")
    public ResponseEntity<List<Expense>> getExpensesByMonth(@PathVariable String month) {
        return expenseService.getExpensesByMonth(month);
    }

    @GetMapping("/year/{year}/month/{month}")
    public ResponseEntity<List<Expense>> getExpensesByYearByMonth(@PathVariable("year") String year, @PathVariable("month") String month) {
        return expenseService.getExpensesByYearByMonth(year, month);
    }

    @PostMapping("/add")
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Expense> updateExpenseById(@PathVariable Integer id, @RequestBody Expense expense) {
        return expenseService.updateExpense(id, expense);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Integer id) {
        return expenseService.deleteExpense(id);
    }

    @GetMapping("/summary/{year}")
    public ResponseEntity<String> getSummaryByYear(@PathVariable Integer year) {
        return expenseService.getSummaryByYear(year);
    }

    @GetMapping("/summary/year/{year}/month/{month}")
    public ResponseEntity<String> getSummaryByYearByMonth(@PathVariable("year") String year, @PathVariable("month") String month) {
        return expenseService.getSummaryByYearByMonth(year, month);
    }

}
