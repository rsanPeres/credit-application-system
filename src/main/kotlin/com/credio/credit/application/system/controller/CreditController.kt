package com.credio.credit.application.system.controller

import com.credio.credit.application.system.controller.dto.CreditDto
import com.credio.credit.application.system.controller.dto.CreditResponse
import com.credio.credit.application.system.controller.dto.GetByCreditCodeResponse
import com.credio.credit.application.system.entity.Credit
import com.credio.credit.application.system.enummeration.Status
import com.credio.credit.application.system.exception.NotFoundByIdException
import com.credio.credit.application.system.service.ICreditService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditController (
    @Autowired
    private val creditService: ICreditService
) {
    @PostMapping
    fun save(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<CreditResponse> {
        val credit = this.creditService.save(creditDto.toEntity())
        return if(credit.customer != null) {ResponseEntity.status(HttpStatus.CREATED)
            .body(CreditResponse(credit))} else
        {ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(CreditResponse(credit))}
    }

    @GetMapping
    fun getAllByCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditResponse>> {
        return try{
            val creditResponse = this.creditService.findAllByCustomer(customerId).stream()
                .map { credit: Credit -> CreditResponse(credit) }.collect(Collectors.toList())
            ResponseEntity.status(HttpStatus.OK).body(creditResponse)

        }catch (e : NotFoundByIdException){
            print(e.stackTrace)
            print(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(listOf(CreditResponse(UUID.fromString("NOT FOUND"),
                    BigDecimal.valueOf(0),0)))
        }
    }

    @GetMapping("/{creditCode}")
    fun getByCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable creditCode: UUID
    ): ResponseEntity<GetByCreditCodeResponse> {
        return try {
            val credit = this.creditService.findByCreditCode(customerId, creditCode)
            ResponseEntity.status(HttpStatus.OK).body(GetByCreditCodeResponse(credit))
        }catch (e : Exception){
            print(e.stackTrace)
            print(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GetByCreditCodeResponse(UUID.fromString("NOT FOUND"), BigDecimal.valueOf(0),
                    0, Status.REJECT, "notFound@gmail.com", BigDecimal.valueOf(0)))
        }
    }
}