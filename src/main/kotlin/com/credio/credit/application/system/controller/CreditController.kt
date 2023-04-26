package com.credio.credit.application.system.controller

import com.credio.credit.application.system.controller.dto.CreditDto
import com.credio.credit.application.system.controller.dto.CreditResponse
import com.credio.credit.application.system.controller.dto.GetByCreditCodeResponse
import com.credio.credit.application.system.entity.Credit
import com.credio.credit.application.system.service.impl.CreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditController (
    private val creditService: CreditService
) {
    @PostMapping
    fun save(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<String> {
        val credit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved")
    }

    @GetMapping
    fun getAllByCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditResponse>> {
        val creditResponse = this.creditService.findAllByCustomer(customerId).stream()
            .map { credit: Credit -> CreditResponse(credit) }.collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditResponse)
    }

    @GetMapping("/{creditCode}")
    fun getByCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable creditCode: UUID
    ): ResponseEntity<GetByCreditCodeResponse> {
        val credit = this.creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.status(HttpStatus.OK).body(GetByCreditCodeResponse(credit))
    }
}