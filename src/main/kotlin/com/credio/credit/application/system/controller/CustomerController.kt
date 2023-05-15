package com.credio.credit.application.system.controller

import com.credio.credit.application.system.controller.dto.CustomerDto
import com.credio.credit.application.system.controller.dto.CustomerRespose
import com.credio.credit.application.system.controller.dto.CustomerUpdateDto
import com.credio.credit.application.system.entity.Customer
import com.credio.credit.application.system.service.ICustomerService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    @Autowired
    private val customerService: ICustomerService
) {
    @PostMapping
    fun save(@RequestBody @Valid customer: CustomerDto): ResponseEntity<CustomerRespose> {
        val savedCustumer = this.customerService.save(customer.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerRespose(savedCustumer))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<CustomerRespose> {
        return try {
            val customer: Customer = this.customerService.findById(id)
            ResponseEntity.status(HttpStatus.OK).body(CustomerRespose(customer))
        }catch (e : Exception){
            print(e.stackTrace)
            print(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomerRespose("", "", "", BigDecimal.valueOf(0),
                    "", "", "", 0L))
        }
    }

    @GetMapping("/email/{email}")
    fun getByEmail(@PathVariable email: String): ResponseEntity<CustomerRespose> {
        return try {
            val customer: Customer = this.customerService.getByEmail(email)
            ResponseEntity.status(HttpStatus.OK).body(CustomerRespose(customer))
        } catch (e : Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomerRespose("", "", "", BigDecimal.valueOf(0),
                    "", "", "", 0L))
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long){
        try {
            val customer = customerService.findById(id)
            customerService.delete(id)
        } catch (e : Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomerRespose("", "", "", BigDecimal.valueOf(0),
                    "", "", "", 0L))
        }
    }

    @PatchMapping
    fun update(
        @RequestParam(value = "customerId") id: Long,
        @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
    ): ResponseEntity<CustomerRespose> {
        return try {
            val customer = this.customerService.findById(id)
            val customerUpdated = this.customerService.save(customerUpdateDto.toEntity(customer))
            ResponseEntity.status(HttpStatus.OK).body(CustomerRespose(customerUpdated))
        }catch (e : Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomerRespose("", "", "", BigDecimal.valueOf(0),
                    "", "", "", 0L))
        }
    }

}