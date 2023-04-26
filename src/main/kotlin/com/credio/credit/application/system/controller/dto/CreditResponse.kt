package com.credio.credit.application.system.controller.dto

import com.credio.credit.application.system.entity.Credit
import com.credio.credit.application.system.enummeration.Status
import java.math.BigDecimal
import java.util.UUID

data class CreditResponse(
    val creditCode : UUID,
    val creditValue : BigDecimal,
    val numberOfInstallments : Int
) {
    constructor(credit: Credit) : this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments
    )
}