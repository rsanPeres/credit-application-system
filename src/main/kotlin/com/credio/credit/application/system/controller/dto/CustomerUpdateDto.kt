package com.credio.credit.application.system.controller.dto

import com.credio.credit.application.system.entity.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "firtname null or empty") val firstName: String,
    @field:NotEmpty(message = "lastName null or empty") val lastName: String,
    @field:NotNull(message = "income null or empty") val income: BigDecimal,
    @field:NotEmpty(message = "zipCode null or empty") val zipCode: String,
    @field:NotEmpty(message = "street null or empty") val street: String
) {
    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.street = this.street
        customer.address.zipCode = this.zipCode
        return customer
    }

}
