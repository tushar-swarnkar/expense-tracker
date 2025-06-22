package com.stew.expense_tracker.service;

import com.stew.expense_tracker.Constants.UserRoles;
import com.stew.expense_tracker.CustomExceptions.UserAlreadyExists;
import com.stew.expense_tracker.JWT.CustomUserDetailsService;
import com.stew.expense_tracker.JWT.JwtUtil;
import com.stew.expense_tracker.model.Expense;
import com.stew.expense_tracker.model.User;
import com.stew.expense_tracker.repository.ExpenseDao;
import com.stew.expense_tracker.repository.UserDao;
import com.stew.expense_tracker.wrapper.ExpenseWrapper;
import com.stew.expense_tracker.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    ExpenseDao expenseDao;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseEntity<String> signup(User user) {
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
            user.setRoles(List.of(UserRoles.USER));
            userDao.save(user);

            return new ResponseEntity<>("Signup successful!", HttpStatus.OK);
        } catch (UserAlreadyExists e) {
            log.error("User already exists!!", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            log.error("Validation failed!", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error!", e);
            return new ResponseEntity<>("An unexpected error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> login(UserWrapper user) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            if (auth.isAuthenticated()) {
                String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(user.getEmail()).getUsername());
                return new ResponseEntity<>("Your token: " + token, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Unexpected error occurred!", e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<User> findUserById(Integer id) {
        try {
            Optional<User> user = userDao.findById(id);
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error finding user with id {}", id, e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<ExpenseWrapper> addExpense(Expense expense) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // user email
            User user = userDao.findByEmail(username).get();

            if (expense.getAmount() <= 0) {
                throw new IllegalArgumentException("Amount cannot be <= 0!!");
            } else if (expense.getDescription().isBlank()) {
                throw new IllegalArgumentException(("Description cannot be empty!!"));
            } else if (expense.getYear() <= 2020 || expense.getYear() > Year.now().getValue()) {
                throw new IllegalArgumentException("Enter correct year!!");
            }

            expense.setUser(user);

            List<Expense> expenseList = user.getExpenseList(); // expense list from db
            expenseList.add(expense);
            expenseDao.save(expense);

            ExpenseWrapper expenseWrapper = new ExpenseWrapper();

            for (Expense exp : expenseList) {
                expenseWrapper.setAmount(exp.getAmount());
                expenseWrapper.setDescription(exp.getDescription());
                expenseWrapper.setMonth(exp.getMonth());
                expenseWrapper.setYear(exp.getYear());
            }

            return new ResponseEntity<>(expenseWrapper, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error adding the expense details!", e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<ExpenseWrapper>> getAllExpenses() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String username = authentication.getName(); // user email
            Optional<User> user = userDao.findByEmail(username);

            List<Expense> expenseList = user.get().getExpenseList();

            List<ExpenseWrapper> expenseWrapperList = new ArrayList<>();

            if (!expenseList.isEmpty()) {
                for (Expense exp : expenseList) {
                    ExpenseWrapper wrapper = new ExpenseWrapper();
                    wrapper.setAmount(exp.getAmount());
                    wrapper.setDescription(exp.getDescription());
                    wrapper.setMonth(exp.getMonth());
                    wrapper.setYear(exp.getYear());
                    expenseWrapperList.add(wrapper);
                }
                return new ResponseEntity<>(expenseWrapperList, HttpStatus.OK);
            }
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error finding the expense list", e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Expense>> getExpenseByMonth(String month) {
        try {
            Month m = Month.valueOf(month.toUpperCase());

            List<Expense> expenses = expenseDao.findByMonth(m);
            if (!expenses.isEmpty()) {
                return new ResponseEntity<>(expenses, HttpStatus.OK);
            }
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error fetching the expense details for the month {}", month, e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Expense>> getExpenseByYearByMonth(String year, String month) {
        try {
            String m = Month.valueOf(month.toUpperCase()).toString();

            List<Expense> expenses = expenseDao.findByYearByMonth(year, month);
            if (!expenses.isEmpty()) {
                return new ResponseEntity<>(expenses, HttpStatus.OK);
            }
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error finding expense list for the year {} and month {}", year, month, e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> expensesByMonth(String month) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<User> user = userDao.findByEmail(email);

            Month m = Month.valueOf(month.toUpperCase());
            int total = 0;

            List<Expense> expenses = expenseDao.findByMonth(m);
            if (!expenses.isEmpty()) {
                for (Expense e : expenses) {
                    total += e.getAmount();
                }
                return new ResponseEntity<>("Your expenses for the month " + month + ": " + total, HttpStatus.OK);
            }
            return new ResponseEntity<>("Could not find expenses for the month " + month, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error calculating total expenditure for the month {}", month, e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> expensesByYear(Integer year) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<User> user = userDao.findByEmail(email);

            int total = 0;

            List<Expense> expenses = expenseDao.findByYear(year);
            if (!expenses.isEmpty()) {
                for (Expense e : expenses) {
                    total += e.getAmount();
                }
                return new ResponseEntity<>("Your expenses for the year " + year + ": " + total, HttpStatus.OK);
            }
            return new ResponseEntity<>("Could not find expenses for the year " + year, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error calculating total expenditure for the year {}", year, e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
