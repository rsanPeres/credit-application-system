package com.credio.credit.application.system

import com.credio.credit.application.system.controller.dto.CreditDto
import com.credio.credit.application.system.controller.dto.CustomerDto
import com.credio.credit.application.system.controller.dto.CustomerUpdateDto
import com.credio.credit.application.system.entity.Address
import com.credio.credit.application.system.entity.Credit
import com.credio.credit.application.system.entity.Customer
import com.credio.credit.application.system.enummeration.Role
import jakarta.persistence.EntityManager
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.Date

object EntityFactory {
    fun buildCustomer(
        firstName : String = "Rafa",
        lastName : String = "Peres",
        cpf : String = "31739084497",
        email : String = "rsan@gmail.com",
        income : BigDecimal = BigDecimal.valueOf(1000.0),
        password : String = "12345",
        zipCode : String = "38408220",
        street : String = "new street",
        role : Role = Role.USER,
        id : Long = 1
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street
        ),
        role = role,
        id = id
    )

    fun buildCredit(
        creditValue : BigDecimal = BigDecimal.valueOf(500.0),
        dayFirstInstallment : LocalDate = LocalDate.of(2023, Month.MAY, 13),
        numberOfInstallments : Int = 5,
        customer : Customer
    ) : Credit = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )

    fun buildCustomerDto(
        firstName: String = "Rafa",
        lastName: String = "Peres",
        cpf: String = "73306506923",
        email: String = "rsan@gmail.com",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        password: String = "123456",
        zipCode: String = "38408222",
        street : String = "New street",
        role : String = "USER",
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street,
        role = role
    )

    fun buildCustomerUpdateDto(
        firstName: String = "Rafaela",
        lastName: String = "Peres",
        income: BigDecimal = BigDecimal.valueOf(3000.0),
        zipCode: String = "38408222",
        street: String = "new new street"
    ) = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )

    fun buildCreditDto(
        creditValue : BigDecimal = BigDecimal.valueOf(3000.0),
        dayFirstInstallment : LocalDate = LocalDate.of(2023, Month.JUNE, 13),
        numberOfInstallments : Int = 10,
        custumerId : Long
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = custumerId
    )
}