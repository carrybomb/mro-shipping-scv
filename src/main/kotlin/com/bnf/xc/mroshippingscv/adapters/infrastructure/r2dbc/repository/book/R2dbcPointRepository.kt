package com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository.book

import com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository.SpringDataR2dbcPointRepositories
import com.bnf.xc.mroshippingscv.domain.model.Point
import com.bnf.xc.mroshippingscv.domain.repository.LibraryPointRepositories
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository

@Repository
class R2dbcPointRepository(
    val r2dbcTemplate: R2dbcEntityTemplate,
    val SpringDataR2dbcPointRepositories : SpringDataR2dbcPointRepositories
) : LibraryPointRepositories {
    override suspend fun insertPoint(point: Point) {
        r2dbcTemplate.insert(point).awaitSingle()
    }

    override suspend fun savePoints(points: List<Point>) {
        SpringDataR2dbcPointRepositories.saveAll(points).asFlow().toList()
    }
}