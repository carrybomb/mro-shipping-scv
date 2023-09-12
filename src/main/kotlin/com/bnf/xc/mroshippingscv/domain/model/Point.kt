package com.bnf.xc.mroshippingscv.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "point_history")
data class Point(
    @Id
    val pointNo: Long? = null,
    val userIdFk: String,
    val variablePoint: Long,
    val cumulativePoint: Long,
    var changeDate: LocalDateTime = LocalDateTime.now()
)