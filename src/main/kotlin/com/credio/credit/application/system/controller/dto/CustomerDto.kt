package com.credio.credit.application.system.controller.dto

import com.credio.credit.application.system.entity.Address
import com.credio.credit.application.system.entity.Customer
import java.math.BigDecimal

data class CustomerDto(
    val firstName : String,
    val lastName : String,
    val cpf : String,
    val income : BigDecimal,
    val email : String,
    val password : String,
    val zipCode : String,
    val street : String
){
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        email = this.email,
        income = this.income,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}
