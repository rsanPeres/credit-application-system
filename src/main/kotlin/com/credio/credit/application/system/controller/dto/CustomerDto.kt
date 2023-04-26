package com.credio.credit.application.system.controller.dto

import com.credio.credit.application.system.entity.Address
import com.credio.credit.application.system.entity.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "invalid firstName") val firstName: String,
    @field:NotEmpty(message = "invalid lastName") val lastName: String,
    @field:CPF(message = "invalid CPF") val cpf: String,
    @field:NotNull(message = "invalid income") val income: BigDecimal,
    @field:Email(message = "Invalid email")
    @field:NotEmpty(message = "email null or empty") val email: String,
    @field:NotEmpty(message = "invalid password") val password: String,
    @field:NotEmpty(message = "invalid zipCode") val zipCode: String,
    @field:NotEmpty(message = "invalid street") val street: String
) {
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
