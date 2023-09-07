package com.bnf.xc.mroshippingscv.adapters.interfaces.rest.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class ReturnRequest(
    @field:Schema(description = "책 넘버 리스트")
    val bookNo: List<Long>
)