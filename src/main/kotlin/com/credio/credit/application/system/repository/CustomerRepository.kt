package com.credio.credit.application.system.repository

import com.credio.credit.application.system.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CustomerRepository : JpaRepository<Customer, Long> {
    @Query("SELECT e FROM CUSTOMER e JOIN FETCH e.roles WHERE e.email = ?1", nativeQuery = true)
    fun getByEmail(email : String) : Customer

    fun existsByEmail(email : String) : Boolean
}