package com.credio.credit.application.system.service

import com.credio.credit.application.system.EntityFactory
import com.credio.credit.application.system.entity.Address
import com.credio.credit.application.system.entity.Customer
import com.credio.credit.application.system.enummeration.Role
import com.credio.credit.application.system.exception.NotFoundByIdException
import com.credio.credit.application.system.repository.CustomerRepository
import com.credio.credit.application.system.service.impl.CustomerService
import io.github.tf2jaguar.micro.core.error.BusinessException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*


@ActiveProfiles("test")
@ExtendWith(io.mockk.junit5.MockKExtension::class)
class CustomerServiceTest() {
    @MockK lateinit var repository: CustomerRepository
    @InjectMockKs lateinit var service: CustomerService
    private val encoder: PasswordEncoder = BCryptPasswordEncoder()
    private final val FAKEID : Long = Random().nextLong()

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
    fun findById_ShouldNotFindIdAndThrowsBusinessException(){
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