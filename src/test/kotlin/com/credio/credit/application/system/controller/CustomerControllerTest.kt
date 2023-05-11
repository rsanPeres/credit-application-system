package com.credio.credit.application.system.controller

import com.credio.credit.application.system.EntityFactory
import com.credio.credit.application.system.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
import java.util.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerControllerTest {
    @Autowired
    private lateinit var repository : CustomerRepository
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper : ObjectMapper

    private val dto = EntityFactory.buildCustomerDto()

    companion object{
        const val URL : String = "/api/customers"
        val RANDOMID = Random().nextLong()
    }

    @BeforeEach fun setup() = repository.deleteAll()
    @AfterEach fun tearDown() = repository.deleteAll()

    @Test
    fun save_ShouldCreateACustomerAndReturnStatusCodeCreated(){
        val dtoString = objectMapper.writeValueAsString(dto)

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(dtoString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Rafa"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Peres"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("73306506923"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("rsan@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("38408222"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("new street"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun save_ShouldNotSaveACustomerWithSameCpfAndReturnStatusCodeConflict(){
        repository.save(EntityFactory.buildCustomerDto().toEntity())
        val dtoString = objectMapper.writeValueAsString(dto)

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(dtoString))
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("73306506923"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                .value("class org.springframework.dao.DataIntegrityViolationException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun save_ShouldNotSaveWithFirstNameEmptyAndReturnStatusCodeBadRequest(){
        val dto = EntityFactory.buildCustomerDto("")
        repository.save(EntityFactory.buildCustomerDto().toEntity())
        val dtoString = objectMapper.writeValueAsString(dto)

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(dtoString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("73306506923"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                .value("class org.springframework.web.bind.MethodArgumentNotValidException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun getById_ShouldGetCustomerByIdAndReturnStatusCodeOk(){
        val customer = repository.save(EntityFactory.buildCustomerDto().toEntity())
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Rafa"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Peres"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("73306506923"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("rsan@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("38408222"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("new street"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun getById_ShouldNotGetCustomerByIdAndReturnStatusCodeBadRequest(){

        mockMvc.perform(MockMvcRequestBuilders.get("$URL/$RANDOMID")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("73306506923"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                .value("class com.credio.credit.application.system.exception.NotFoundByIdException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun delete_ShouldDeleteCustomerByIdAndReturnStatusCodeBadRequest(){
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/$RANDOMID")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun delete_ShouldDeleteCustomerByIdAndReturnStatusCodeNoContent(){
        val customer = repository.save(EntityFactory.buildCustomerDto().toEntity())
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customer.id}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun update_ShouldUpdateCustomerGivenByIdAndReturnStatusCodeOk(){
        val customer = repository.save(EntityFactory.buildCustomerDto().toEntity())
        val customerUpdateDto = EntityFactory.buildCustomerUpdateDto()
        val dtoString = objectMapper.writeValueAsString(customerUpdateDto)

        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dtoString))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Rafaela"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Peres"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("73306506923"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("rsan@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(3000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("38408222"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("new new street"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun update_ShouldNotUpdateCustomerAndReturnStatusCodeBadRequest(){
        val customerUpdateDto = EntityFactory.buildCustomerUpdateDto()
        val dtoString = objectMapper.writeValueAsString(customerUpdateDto)

        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=$RANDOMID")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dtoString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("73306506923"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                .value("class com.credio.credit.application.system.exception.NotFoundByIdException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }
}