package com.credio.credit.application.system.service.impl

import com.credio.credit.application.system.entity.Credit
import com.credio.credit.application.system.exception.NotFoundByIdException
import com.credio.credit.application.system.repository.CreditRepository
import com.credio.credit.application.system.service.ICreditService
import com.credio.credit.application.system.service.ICustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class CreditService(
    @Autowired
    private val creditRepository: CreditRepository,
    @Autowired
    private val customerService: ICustomerService
) : ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            if(credit.customer != null && credit.customer?.id != null) {
                customer = customerService.findById(credit.customer?.id!!)
                return creditRepository.save(credit)
            }

            return Credit(dayFirstInstallment = LocalDate.now())
        }
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> {
        val credits = creditRepository.findAllByCustomerId(customerId)
        return credits.ifEmpty {
            throw NotFoundByIdException("Customer not found or has no credits")
        }

    }

    override fun findByCreditCode(customerId : Long, creditCode: UUID): Credit {
        val credit : Credit = this.creditRepository.findByCreditCode(creditCode)
            ?: throw IllegalArgumentException("CreditCode $creditCode not found")
        return if (credit.customer?.id == customerId) credit else throw RuntimeException("Contact admin")
    }
}