package com.credio.credit.application.system.controller

import com.credio.credit.application.system.EntityFactory
import com.credio.credit.application.system.repository.CreditRepository
import com.credio.credit.application.system.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CreditControllerTest {
    @Autowired
    private lateinit var repository: CreditRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val customer = EntityFactory.buildCustomer()

    companion object {
        const val URL: String = "/api/credits"
        val RANDOMID = Random().nextLong()
    }

    @BeforeEach
    fun setup() {
        repository.deleteAll()
        customerRepository.save(customer)
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

    @Test
    @Order(1)
    fun save_ShouldCreateACreditAndReturnStatusCodeCreated(){
        val dto = EntityFactory.buildCreditDto(custumerId = 1)
        val dtoString = objectMapper.writeValueAsString(dto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(3000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(10))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @Order(4)
    fun save_ShouldNotSaveACreditReturnAEmptyCreditAndStatusCodeBadRequest(){
        val dto = EntityFactory.buildCreditDto(custumerId = RANDOMID)
        val dtoString = objectMapper.writeValueAsString(dto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(dtoString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                .value("class com.credio.credit.application.system.exception.NotFoundByIdException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @Order(2)
    fun getAllByCustomerId_ShouldGetCreditByCustomerIdAndReturnStatusCodeOk(){
        val credit = repository.save(EntityFactory.buildCreditDto(custumerId = 1).toEntity())
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${customer.id}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(3000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value(10))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @Order(5)
    fun getAllByCustomerId_ShouldNotGetCustomerByIdAndReturnStatusCodeBadRequest(){

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${RANDOMID}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                .value("class java.lang.IllegalArgumentException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @Order(3)
    fun getByCreditCode_ShouldGetCreditByCodeAndReturnStatusCodeOk(){
        val credit = repository.save(EntityFactory.buildCreditDto(custumerId = 1).toEntity())

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=${credit.customer?.id}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").value("${credit.creditCode}"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(3000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("rsan@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(1000.0))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @Order(6)
    fun getByCreditCode_ShouldNotGetCreditAndReturnStatusCodeBadRequest(){

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/eaecb119-fd0b-4cdd-88b3-48a44d14ed7e?customerId=$RANDOMID")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                .value("class java.lang.IllegalArgumentException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }
}