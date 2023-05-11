package com.credio.credit.application.system.service

import com.credio.credit.application.system.entity.Credit
import com.credio.credit.application.system.entity.Customer
import java.util.UUID

interface ICustomerService {
    fun save(customer : Customer) : Customer
    fun findById(id: Long) : Customer
    fun delete(id : Long)
    fun getByEmail(email : String) : Customer
    fun existsByEmail(email : String) : Boolean

}