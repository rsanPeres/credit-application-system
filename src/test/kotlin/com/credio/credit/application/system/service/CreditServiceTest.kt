package com.credio.credit.application.system.service

import com.credio.credit.application.system.EntityFactory
import com.credio.credit.application.system.entity.Credit
import com.credio.credit.application.system.entity.Customer
import com.credio.credit.application.system.exception.NotFoundByIdException
import com.credio.credit.application.system.repository.CreditRepository
import com.credio.credit.application.system.service.impl.CreditService
import com.credio.credit.application.system.service.impl.CustomerService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("test")
@ExtendWith(io.mockk.junit5.MockKExtension::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CreditServiceTest{
    @MockK
    lateinit var repository: CreditRepository
    @Autowired
    lateinit var service: ICreditService
    @RelaxedMockK
    lateinit var customerService: CustomerService

    private val creditCode1 = UUID.fromString("757094ea-2d1c-47d0-9604-e26cbb01c753")
    private val creditCode2 = UUID.fromString("c2d1e2b5-3d4c-406c-8a87-0a5f654920fe")
    private val customer = EntityFactory.buildCustomer()
    private val credit1 = EntityFactory.buildCredit(customer = customer)
    private val credit2 = EntityFactory.buildCredit(customer = customer)

    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this)
        service = CreditService(repository, customerService)
    }

    @Test
    fun save_givenACreditShouldSavaAndReturnAsExpected(){
        val fakeCustomer = EntityFactory.buildCustomer()
        val fakeCredit = EntityFactory.buildCredit(customer = fakeCustomer)
        fakeCredit.creditCode = creditCode1
        every { repository.save((any())) } returns fakeCredit

        val saved = service.save(fakeCredit)

        Assertions.assertThat(saved).isNotNull
        Assertions.assertThat(saved.creditCode).isSameAs(creditCode1)
        verify(exactly = 1) { repository.save(fakeCredit) }
    }

    @Test
    fun save_GivenANotFindCustomerShouldNotSaveAndReturnACreditBlank(){
        val blankCustomer = Customer()
        val credit = EntityFactory.buildCredit(customer = blankCustomer)
        credit.creditCode = creditCode1
        every { customerService.findById((any())) } returns blankCustomer

        val saved = service.save(credit)

        Assertions.assertThat(saved).isNotNull
        Assertions.assertThat(saved.creditCode).isNotEqualTo(creditCode1)
        Assertions.assertThat(saved.customer).isNull()
        verify(exactly = 0) { repository.save(credit) }
    }

    @Test
    fun findAllByCustomer_GivenACustomerIdShouldReturnAsExpected(){
        credit1.creditCode = creditCode1
        credit2.creditCode = creditCode2
        val credits = listOf<Credit>(credit1, credit2)
        every { repository.findAllByCustomerId((customer.id!!)) } returns credits

        val result = service.findAllByCustomer(customer.id!!)

        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.size).isEqualTo(2)
        Assertions.assertThat(result.first().creditCode).isEqualTo(creditCode1)
        Assertions.assertThat(result.last().creditCode).isEqualTo(creditCode2)
        Assertions.assertThat(result.first().customer?.password).isEqualTo(customer.password)
        Assertions.assertThat(result.last().customer?.password).isEqualTo(customer.password)
        verify(exactly = 1) { repository.findAllByCustomerId(customer.id!!) }
    }

    @Test
    @Order(1)
    fun findAllByCustomer_GivenAInvalidCustomerIdShouldThrowsNotFoundIdException(){
        every { repository.findAllByCustomerId((customer.id!!)) } returns listOf()

        Assertions.assertThatExceptionOfType(NotFoundByIdException::class.java)
            .isThrownBy { service.findAllByCustomer(customer.id!!) }
            .withMessage("Customer not found or has no credits")
    }

    @Test
    fun findByCreditCode_GivenACustomerIdAndCreditCodeShouldReturnAsExpected(){
        credit1.creditCode = creditCode1
        every { repository.findByCreditCode(creditCode1) } returns credit1

        val result = service.findByCreditCode(credit1.customer?.id!!, creditCode1)

        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.creditCode).isEqualTo(creditCode1)
        Assertions.assertThat(result.customer?.password).isEqualTo(customer.password)
        verify(exactly = 1) { repository.findByCreditCode(creditCode1) }
    }

    @Test
    fun findByCreditCode_GivenAInvalidCustomerIdShouldThrowsRuntimeException(){
        val creditCode = UUID.randomUUID()
        credit1.customer?.id = 33
        every { repository.findByCreditCode(creditCode) } returns credit1

        Assertions.assertThatExceptionOfType(RuntimeException::class.java)
            .isThrownBy { service.findByCreditCode(1, creditCode) }
            .withMessage("Contact admin")
    }

    @Test
    fun findByCreditCode_GivenAInvalidCreditCodeShouldThrowsIllegalArgumentException(){
        val creditCode = UUID.randomUUID()
        every { repository.findByCreditCode(creditCode) } returns null

        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { service.findByCreditCode(customer.id!!, creditCode) }
            .withMessage("CreditCode $creditCode not found")
    }
}

