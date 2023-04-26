package com.credio.credit.application.system.controller

import com.credio.credit.application.system.controller.dto.CustomerDto
import com.credio.credit.application.system.controller.dto.CustomerRespose
import com.credio.credit.application.system.controller.dto.CustomerUpdateDto
import com.credio.credit.application.system.entity.Customer
import com.credio.credit.application.system.service.impl.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping
    fun save(@RequestBody @Valid customer: CustomerDto): ResponseEntity<String> {
        val savedCustumer = this.customerService.save(customer.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer ${savedCustumer.email} saved!")
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<CustomerRespose> {
        val customer: Customer = this.customerService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(CustomerRespose(customer))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = this.customerService.delete(id)

    @PatchMapping
    fun update(
        @RequestParam(value = "customerId") id: Long,
        @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
    ): ResponseEntity<CustomerRespose> {
        val customer = this.customerService.findById(id)
        val customerUpdated = this.customerService.save(customerUpdateDto.toEntity(customer))
        return ResponseEntity.status(HttpStatus.OK).body(CustomerRespose(customerUpdated))
    }

}