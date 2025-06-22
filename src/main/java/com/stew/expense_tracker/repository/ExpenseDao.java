package com.stew.expense_tracker.repository;

import com.stew.expense_tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

@Repository
public interface ExpenseDao extends JpaRepository<Expense, Integer> {
    List<Expense> findByMonth(Month month);

    List<Expense> findByYear(int year);

    @Query(nativeQuery = true, value = "select * from expense e where e.year=:year and e.month=:month ")
    List<Expense> findByYearByMonth(String year, String month);
}
