package com.stew.expense_tracker.service;

import com.stew.expense_tracker.repository.ExpenseDao;
import com.stew.expense_tracker.model.Expense;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExpenseService {

    @Autowired
    ExpenseDao expenseDao;

    public ResponseEntity<List<Expense>> getAllExpense() {
//        Expense expense = new Expense();
//        expense.setMonth(LocalDate.now().getMonth().toString());
        try {
            List<Expense> expenses = expenseDao.findAll();
            if (!expenses.isEmpty()) {
                return new ResponseEntity<>(expenses, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error finding all expenses");
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Expense> getExpenseById(Integer id) {
        try {
            Optional<Expense> expense = expenseDao.findById(id);
            if (expense.isPresent()) {
                return new ResponseEntity<>(expense.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error finding expense with id: {}", id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Expense>> getExpensesByMonth(String month) {
        try {
            Month m = Month.valueOf(month.toUpperCase());

            List<Expense> expenses = expenseDao.findByMonth(m);
            if (!expenses.isEmpty()) {
                return new ResponseEntity<>(expenses, HttpStatus.OK);
            }
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error finding expenses of month {}", month);
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Expense>> getExpensesByYear(Integer year) {
        try {
            List<Expense> expenses = expenseDao.findByYear(year);
            if (!expenses.isEmpty()) {
                return new ResponseEntity<>(expenses, HttpStatus.OK);
            }
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error finding expenses of year {}", year);
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Expense>> getExpensesByYearByMonth(String year, String month) {
        try {
            List<Expense> expenses = expenseDao.findByYearByMonth(year, month);
            if (!expenses.isEmpty()) {
                return new ResponseEntity<>(expenses, HttpStatus.OK);
            }
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error find expenses by year {} and month {}", year, month);
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Expense> addExpense(Expense expense) {
        try {
            if (expense.getDescription() == null || expense.getDescription().trim().isEmpty()) {
                throw new IllegalArgumentException("Description cannot be empty!!");
            }
            if (expense.getAmount() <= 0) {
                throw new IllegalArgumentException("Amount cannot be empty!!");
            }
            if (expense.getYear() < 2020 || expense.getYear() > Year.now().getValue()) {
                throw new IllegalArgumentException("Invalid year entered!");
            }

            return new ResponseEntity<>(expenseDao.save(expense), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error logging the expense!", e);
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Expense> updateExpense(Integer id, Expense expense) {
        try {
            Optional<Expense> oldExpense = expenseDao.findById(id);
            if (oldExpense.isPresent()) {
                oldExpense.get().setAmount(expense.getAmount());
                oldExpense.get().setDescription(expense.getDescription());

                return new ResponseEntity<>(expenseDao.save(oldExpense.get()), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating expense details of id {}", id, e);
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> deleteExpense(Integer id) {
        try {
            Optional<Expense> expense = expenseDao.findById(id);
            if (expense.isPresent()) {
                expenseDao.delete(expense.get());
                return new ResponseEntity<>("Successfully removed the expense with id: " + id, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error deleting expense with id: {}", id, e);
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> getSummaryByYear(Integer year) {
        try {
            List<Expense> expenses = expenseDao.findByYear(year);
            int total = 0;
            if (!expenses.isEmpty()) {
                for (Expense e : expenses) {
                    total += e.getAmount();
                }
                return new ResponseEntity<>("Total expenditure in the year " + year + ": " + total + "/-", HttpStatus.OK);
            }
            return new ResponseEntity<>("Cannot find expenses for the year" + year, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error summarizing your expenses for the year {}", year, e);
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> getSummaryByYearByMonth(String year, String month) {
        try {
            List<Expense> expenses = expenseDao.findByYearByMonth(year, month);
            int total = 0;
            if (!expenses.isEmpty()) {
                for (Expense e : expenses) {
                    total += e.getAmount();
                }
                return new ResponseEntity<>("Your expenses for the year " + year + " and month " + month + ": " + total + "/-", HttpStatus.OK);
            }
            return new ResponseEntity<>("Cannot find expenses in the year " + year + " and month " + month, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error summarizing your expenses for the year {} and month {}", year, month, e);
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

