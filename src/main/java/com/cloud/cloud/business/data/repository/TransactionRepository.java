package com.cloud.cloud.business.data.repository;

import com.cloud.cloud.business.data.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByProfileId(long l);
}
