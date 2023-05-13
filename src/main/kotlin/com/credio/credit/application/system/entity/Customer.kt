package com.credio.credit.application.system.entity

import com.credio.credit.application.system.enummeration.Role
import jakarta.persistence.*
import org.springframework.boot.autoconfigure.domain.EntityScan
import java.math.BigDecimal

@Entity
@EntityScan
data class Customer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id : Long? = null,
    @Column(nullable = false) var firstName : String = "",
    @Column(nullable = false) var lastName : String = "",
    @Column(nullable = false, unique = true) var cpf : String = "",
    @Column(nullable = false, unique = true) var email : String = "",
    @Column(nullable = false) var income : BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false) var password : String = "",
    @Embedded
    @AttributeOverrides(value = [
        AttributeOverride(name = "zipCode", column =  Column(name = "zip_code")),
        AttributeOverride(name = "street", column = Column(name = "street"))
    ])
    var address: Address = Address(),
    @Column(nullable = false) @OneToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.REMOVE), mappedBy = "customer") var credits: List<Credit> = mutableListOf(),
    @Enumerated var role : Role = Role.USER
)

