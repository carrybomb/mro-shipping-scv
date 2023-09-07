package com.bnf.xc.mroshippingscv.adapters.interfaces.rest.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class CreateUserRequest(
    @field:Schema(description = "아이디")
    val userId: String,
    @field:Schema(description = "비밀번호")
    val userPw: String,
    @field:Schema(description = "이름")
    val userName: String,
    @field:Schema(description = "전화번호")
    val userTel: String,
    @field:Schema(description = "생년월일")
    val userBirth: String
)

data class UpdateUserRequest(
    @field:Schema(description = "아이디")
    val userId: String,
    @field:Schema(description = "비밀번호")
    val userPw: String,
    @field:Schema(description = "이름")
    val userName: String,
    @field:Schema(description = "전화번호")
    val userTel: String,
    @field:Schema(description = "생년월일")
    val userBirth: String
)