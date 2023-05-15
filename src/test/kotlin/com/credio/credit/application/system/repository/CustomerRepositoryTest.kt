package com.credio.credit.application.system.repository

import com.credio.credit.application.system.EntityFactory
import com.credio.credit.application.system.entity.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {
    @Autowired
    lateinit var repository: CustomerRepository
    private lateinit var customer: Customer
    private final val email : String = "rsan@gmail.com"

    @BeforeEach
    fun setup () {
        customer = repository.save(EntityFactory.buildCustomer())
    }

    @Test
    fun getByEmail_GivenACustomerSavedShouldReturnAsExpected(){
        val customer = repository.getByEmail(email)

        Assertions.assertThat(email).isSameAs(customer.get().email)
        Assertions.assertThat(customer).isNotEmpty.isNotNull
    }
}