package com.cetpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cetpa.entities.TransactionSummary;

public interface SummaryRepository extends JpaRepository<TransactionSummary,Integer> 
{
}
