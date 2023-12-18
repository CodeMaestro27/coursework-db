package com.example.db.repos;

import com.example.db.domain.Article;
import com.example.db.domain.Balance;
import com.example.db.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OperationRepository extends JpaRepository<Operation, Integer> {

    Operation findFirstByBalance(Balance balance);

    Operation findFirstByArticle(Article article);

}
