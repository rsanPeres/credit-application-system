package com.credio.credit.application.system.controller.dto

import com.credio.credit.application.system.entity.Customer
import java.math.BigDecimal

class CustomerRespose(
    val firstName : String,
    val lastName : String,
    val cpf : String,
    val income : BigDecimal,
    val email : String,
    val zipCode : String,
    val street : String
) {
    constructor(customer: Customer) : this(
        firstName = customer.firstName,
        lastName = customer.lastName,
        cpf = customer.cpf,
        income = customer.income,
        email = customer.email,
        zipCode = customer.address.zipCode,
        street = customer.address.street
    )
}
