package com.credio.credit.application.system.service.impl

import com.credio.credit.application.system.entity.Customer
import com.credio.credit.application.system.exception.NotFoundByIdException
import com.credio.credit.application.system.repository.CustomerRepository
import com.credio.credit.application.system.service.ICustomerService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val encoder : PasswordEncoder
) : ICustomerService {
    override fun save(customer: Customer): Customer {
        customer.password = encoder.encode(customer.password)
        return this.customerRepository.save(customer)
    }


    override fun findById(id: Long): Customer =
        this.customerRepository.findById(id).orElseThrow{
            throw NotFoundByIdException("Id $id not found")
        }


    override fun delete(id: Long){
        val customer = this.customerRepository.findById(id)

        if (customer.get().email.isNotEmpty()) this.customerRepository.delete(customer.get()) else throw NotFoundByIdException("Customer not found")

    }

    override fun getByEmail(email: String): Customer {
        TODO("Not yet implemented")
    }

    override fun existsByEmail(email: String): Boolean {
        TODO("Not yet implemented")
    }

}