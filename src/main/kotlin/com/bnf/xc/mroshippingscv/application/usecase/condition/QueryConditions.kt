package com.bnf.xc.mroshippingscv.application.usecase.condition

import org.springframework.data.domain.Pageable


data class BookQueryCondition(
    val pageable: Pageable,
    val bookName: String = "",
    val author: String = ""
)

data class UserQueryCondition(
    val pageable: Pageable,
    val userName: String
)