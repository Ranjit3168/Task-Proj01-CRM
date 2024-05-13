package com.bpl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bpl.entity.CustomerEntity;

public interface ICustomerRepository extends JpaRepository<CustomerEntity,Long> {

}
