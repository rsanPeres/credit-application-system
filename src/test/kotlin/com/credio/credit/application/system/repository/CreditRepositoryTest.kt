package com.credio.credit.application.system.repository

import com.credio.credit.application.system.EntityFactory
import com.credio.credit.application.system.entity.Credit
import com.credio.credit.application.system.entity.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {
    @Autowired
    lateinit var repository: CreditRepository
    @Autowired
    lateinit var customerRepository: CustomerRepository
    @Autowired
    lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    private final val creditCode1 = UUID.fromString("757094ea-2d1c-47d0-9604-e26cbb01c753")
    private final val creditCode2 = UUID.fromString("c2d1e2b5-3d4c-406c-8a87-0a5f654920fe")


    @BeforeEach fun setup (){
        customer = customerRepository.save(EntityFactory.buildCustomer())
        credit1 = testEntityManager.persist(EntityFactory.buildCredit(customer = customer))
        credit2 = testEntityManager.persist(EntityFactory.buildCredit(customer = customer))

    }

    @Test
    fun findByCreditCode_ShouldFindCreditByCreditCode(){
        credit1.creditCode = creditCode1
        credit2.creditCode = creditCode2

        val fakeCredit1 = repository.findByCreditCode(creditCode1)
        val fakeCredit2 = repository.findByCreditCode(creditCode2)

        Assertions.assertThat(fakeCredit1).isNotNull
        Assertions.assertThat(fakeCredit1).isSameAs(credit1)
        Assertions.assertThat(fakeCredit2).isNotNull
        Assertions.assertThat(fakeCredit2).isSameAs(credit2)
    }

    @Test
    fun findAllByCustomerId_ShouldFindAllCreditsByCustomerId(){
        val customerId = 2L

        val credits = repository.findAllByCustomerId(customerId)

        Assertions.assertThat(credits).isNotNull.isNotEmpty
    }
}


