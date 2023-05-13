package com.credio.credit.application.system.repository

import com.credio.credit.application.system.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface CustomerRepository : JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.email = ?1", nativeQuery = true)
    fun getByEmail(email : String) : Optional<Customer>

    fun existsByEmail(email : String) : Boolean
}