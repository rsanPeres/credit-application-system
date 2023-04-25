package com.credio.credit.application.system.controller

import com.credio.credit.application.system.controller.dto.CustomerDto
import com.credio.credit.application.system.controller.dto.CustomerRespose
import com.credio.credit.application.system.controller.dto.CustomerUpdateDto
import com.credio.credit.application.system.entity.Customer
import com.credio.credit.application.system.service.impl.CustomerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping
    fun save(@RequestBody customer: CustomerDto) : String{
        val savedCustumer = this.customerService.save(customer.toEntity())
        return "Customer ${savedCustumer.email} saved!"
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id : Long) : CustomerRespose {
        val customer : Customer = this.customerService.findById(id)
        return CustomerRespose(customer)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id : Long) = this.customerService.delete(id)

    @PatchMapping
    fun update(@RequestParam(value = "customerId") id : Long,
               @RequestBody customerUpdateDto : CustomerUpdateDto) : CustomerRespose{
        val customer = this.customerService.findById(id)
        val customerUpdated = this.customerService.save(customerUpdateDto.toEntity(customer))
        return CustomerRespose(customerUpdated)
    }

}