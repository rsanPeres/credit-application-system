package com.credio.credit.application.system.service.impl

import com.credio.credit.application.system.entity.Customer
import com.credio.credit.application.system.exception.NotFoundByIdException
import com.credio.credit.application.system.repository.CustomerRepository
import com.credio.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) : ICustomerService {
    override fun save(customer: Customer): Customer =
        this.customerRepository.save(customer)


    override fun findById(id: Long): Customer =
        this.customerRepository.findById(id).orElseThrow{
            throw NotFoundByIdException("Id $id not found")
        }


    override fun delete(id: Long){
        val customer = this.customerRepository.findById(id) as Customer

        if (!customer.email.isNullOrEmpty()) this.customerRepository.delete(customer) else throw NotFoundByIdException("Customer not found")

    }

}