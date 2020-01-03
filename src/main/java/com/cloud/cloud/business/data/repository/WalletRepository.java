package com.cloud.cloud.business.data.repository;

import com.cloud.cloud.business.data.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByProfileId(long l);
}
