package com.example.db.repos;

import com.example.db.domain.Article;
import com.example.db.util.BalanceOperations;
import com.example.db.util.UncalculatedOperations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM uncalculated_operations();")
    List<UncalculatedOperations> getUncalculatedOperations();

    @Query(nativeQuery = true, value = "SELECT * FROM balance_operations_count();")
    List<BalanceOperations> getBalanceOperationsCount();
}
