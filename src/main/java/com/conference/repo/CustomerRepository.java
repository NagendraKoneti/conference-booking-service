package com.conference.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conference.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
