package com.credio.credit.application.system.entity

import com.credio.credit.application.system.enummeration.Role
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
data class Customer(
    @Column(nullable = false) var firstName : String = "",
    @Column(nullable = false) var lastName : String = "",
    @Column(nullable = false, unique = true) var cpf : String = "",
    @Column(nullable = false, unique = true) var email : String = "",
    @Column(nullable = false) var income : BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false) var password : String = "",
    @Column(nullable = false) @Embedded var address: Address = Address(),
    @Column(nullable = false) @OneToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.REMOVE), mappedBy = "customer") var credits: List<Credit> = mutableListOf(),
    @Enumerated var role : Role = Role.USER,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id : Long? = null
)

