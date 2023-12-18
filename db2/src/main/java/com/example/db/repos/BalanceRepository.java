package com.example.db.repos;

import com.example.db.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BalanceRepository extends JpaRepository<Balance, Integer> {

}
