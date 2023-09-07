package com.bnf.xc.mroshippingscv.application.dto

data class PageableTotalDTO<T>(
    val content: T,
    val totalPages: Int,
    val totalElements: Long
)