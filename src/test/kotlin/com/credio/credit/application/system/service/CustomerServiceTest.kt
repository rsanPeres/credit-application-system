package com.credio.credit.application.system.service

import com.credio.credit.application.system.EntityFactory
import com.credio.credit.application.system.entity.Customer
import com.credio.credit.application.system.exception.NotFoundByIdException
import com.credio.credit.application.system.repository.CustomerRepository
import com.credio.credit.application.system.service.impl.CustomerService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.aspectj.lang.annotation.Before
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.util.*


@ActiveProfiles("test")
@ExtendWith(io.mockk.junit5.MockKExtension::class)
class CustomerServiceTest {
    @RelaxedMockK
    lateinit var repository: CustomerRepository
    private val encoder: PasswordEncoder = BCryptPasswordEncoder()
    @Autowired
    lateinit var service: ICustomerService
    private val FAKEID : Long = Random().nextLong()

    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this)
        service = CustomerService(repository, encoder)
    }

    @Test
    fun save_shouldCreateCustomer(){
        //arrange-given
        val fakeCustomer : Customer = EntityFactory.buildCustomer()
        every { repository.save((any())) } returns fakeCustomer

        //act-when
        val save = service.save(fakeCustomer)
        //assert-then
        Assertions.assertThat(save).isNotNull
        Assertions.assertThat(encoder.matches(save.password, fakeCustomer.password))
        verify(exactly = 1) { repository.save(fakeCustomer) }
    }

    @Test
    fun findById_ShouldReturnACustomer(){
        val fakeCustomer = EntityFactory.buildCustomer(id = FAKEID)
        every { repository.findById(FAKEID) } returns Optional.of(fakeCustomer)

        val result = service.findById(FAKEID)

        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(encoder.matches(result.password, fakeCustomer.password))
        verify(exactly = 1) { repository.findById(FAKEID) }
    }

    @Test
    fun findById_ShouldNotFindIdAndThrowsNotFoudByIdException(){
        every { repository.findById(FAKEID) } returns Optional.empty()

        Assertions.assertThatExceptionOfType(NotFoundByIdException::class.java)
            .isThrownBy { service.findById(FAKEID) }
            .withMessage("Id $FAKEID not found")
    }

    @Test
    fun delete_shouldDeleteById(){
        val fakeCustomer = EntityFactory.buildCustomer(id = FAKEID)
        every { repository.findById(FAKEID) } returns Optional.of(fakeCustomer)
        every { repository.delete(fakeCustomer) } just runs

        service.delete(FAKEID)

        verify(exactly = 1) { repository.findById(FAKEID) }
        verify(exactly = 1) { repository.delete(fakeCustomer) }
    }
}