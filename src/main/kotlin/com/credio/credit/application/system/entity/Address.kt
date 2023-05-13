package com.credio.credit.application.system.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import org.springframework.boot.autoconfigure.domain.EntityScan

@Embeddable
@EntityScan
class Address(
    @Column(name = "zip_code", nullable = false) var zipCode : String = "",
    @Column(nullable = false) var street : String = ""
)