package com.bnf.xc.mroshippingscv.domain.model

import com.bnf.xc.mroshippingscv.domain.enum.RankType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table(name = "book_tb")
data class Book(
    @Id
    val bookNo: Long = 0,
    val bookName: String,
    // 대여 상태: 대여-> true, 미대여 -> false
    var rentStatus: Boolean = false,
    val author: String,
    // 책 출간일
    val createDate: LocalDate,
    // 책 구매 일자: insert 할때 입력
    val purchaseDate: LocalDateTime,
    // 책 노출 상태: 노출 -> true, 미노출-> false
    val exist: Boolean = true,
    // 책 수정 날짜
    var updateDate: LocalDateTime? = null,
    val bookRank: RankType
) {
    // 수정 일자
    fun updateBook(): Book {
        updateDate = LocalDateTime.now()
        return this
    }

    fun updateStatus(status: Boolean):Book {
        rentStatus = status
        updateDate = LocalDateTime.now()
        return this
    }
}