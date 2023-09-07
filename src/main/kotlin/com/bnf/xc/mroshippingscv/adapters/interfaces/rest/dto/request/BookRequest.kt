package com.bnf.xc.prepayment.core.adapters.interfaces.rest.dto.request

import com.bnf.xc.mroshippingscv.domain.enum.RankType
import io.swagger.v3.oas.annotations.media.Schema

data class CreateBookRequest(
    @field:Schema(description = "책 이름")
    val bookName: String,
    @field:Schema(description = "작가")
    val author: String,
    @field:Schema(description = "출간일")
    val createDate: String,
    @field:Schema(description = "책 등급")
    val bookRank: RankType
)

data class UpdateBookRequest(
    @field:Schema(description = "책 번호")
    val bookNo: Long,
    @field:Schema(description = "책 이름")
    val bookName: String,
    @field:Schema(description = "책 대여 상태")
    val rentStatus: Boolean,
    @field:Schema(description = "저자")
    val author: String,
    @field:Schema(description = "책 존재 유무")
    val exist: Boolean,
    @field:Schema(description = "책 등급")
    val bookRank: RankType
)

data class ReturnRequest(
    @field:Schema(description = "책 넘버 리스트")
    val bookNos: List<Long>
)