package com.bnf.xc.mroshippingscv.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table(name = "rent_history")
data class RentHistory(
    @Id
    val rentNo: Long? = null,
    val userIdFk: String,
    val bookNoFk: Long,
    val rentDate: LocalDate = LocalDate.now(),
    var returnDate: LocalDate? = null,
    val expectReturnDate: LocalDate = LocalDate.now().plusDays(7)
){
    fun returnBook(): RentHistory {
        returnDate = LocalDate.now()
        return this
    }
}