package com.project.WalletService.repository;

import com.project.WalletService.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet findByContact(String contact);

    @Transactional
    @Modifying
    @Query("update Wallet w set w.balance = w.balance + :amount where w.contact = :contact")
    Integer updateWallet(String contact, Double amount);
}
