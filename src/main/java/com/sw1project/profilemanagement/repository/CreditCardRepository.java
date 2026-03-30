package com.sw1project.profilemanagement.repository;

import com.sw1project.profilemanagement.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}