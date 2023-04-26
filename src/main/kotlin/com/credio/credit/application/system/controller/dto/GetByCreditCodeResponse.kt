package com.credio.credit.application.system.controller.dto

import com.credio.credit.application.system.entity.Credit
import com.credio.credit.application.system.enummeration.Status
import java.math.BigDecimal
import java.util.*

data class GetByCreditCodeResponse(
    val creditCode : UUID,
    val creditValue : BigDecimal,
    val numberOfInstallments : Int,
    val status : Status,
    val emailCustomer : String?,
    val incomeCustomer: BigDecimal?
) {
    constructor(credit: Credit) : this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments,
        status = credit.status,
        emailCustomer = credit.customer?.email,
        incomeCustomer = credit.customer?.income
    )
}
