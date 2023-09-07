package com.bnf.xc.mroshippingscv.adapters.interfaces.rest.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class RentRequest(
    @field:Schema(description = "유저 아이디")
    val userId: String,
    @field:Schema(description = "책 넘버")
    val bookNos: List<Long>
)