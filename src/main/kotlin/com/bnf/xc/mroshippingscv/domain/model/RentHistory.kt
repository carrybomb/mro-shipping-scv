package com.bnf.xc.mroshippingscv.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "rent_history")
data class RentHistory(
    @Id
    val rentNo: Long? = null,
    val userIdFk: String,
    val bookNoFk: Long,
    val rentDate: LocalDate = LocalDate.now(),
    val returnDate: LocalDate? = null,
    val expectReturnDate: LocalDate = LocalDate.now().plusDays(7)
)